package service.entity

import models.entities.Entity
import repository._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import service.validation.{SuccessfulResult, ValidationResult, PropertyValidationError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CashService[E <: Entity[E], T <: Table[E] { val id: Rep[Int] }, G <: GenericCRUD[E, T, F], F] extends EntityService[E, T, G, F] {
  val profitRepo: ProfitRepo
  val operationRepo: OperationRepo
  val refundRepo: RefundRepo
  val rentRepo: RentRepo
  val dbConfig: DatabaseConfig[JdbcProfile]

  import dbConfig.driver.api._

  case class CashState(amount: BigDecimal)

  def getCurrentState: Future[CashState] = {
    val zero = BigDecimal(0)
    // дата последнего снятия кассы
    val lastProfitQuery = profitRepo.tableQuery.sortBy(p => p.changeTime.desc).take(1).map(p => p.changeTime)
    dbConfig.db.run(lastProfitQuery.result) flatMap { dates =>
      // сумма положительных операций после последнего съема кассы
      val operationQuery = (dates.headOption match {
        case Some(lastProfitTime) => operationRepo.tableQuery.filter(o => o.changeTime > lastProfitTime)
        case None => operationRepo.tableQuery
      }).filter(o => o.amount > zero).map(o => o.amount).sum

      // сумма возвратов после последнего съема кассы
      val refundQuery = (dates.headOption match {
        case Some(lastProfitTime) => refundRepo.tableQuery.filter(r => r.changeTime > lastProfitTime)
        case None => refundRepo.tableQuery
      }).map(r => r.amount).sum

      // сумма залогов после последнего снятия кассы

      val depositQuery = (dates.headOption match {
        case Some(lastProfitTime) => rentRepo.tableQuery filter(_.creationDate > lastProfitTime)
        case None => rentRepo.tableQuery
      }).map(_.deposit).sum

      // расчетная сумма в кассе
      for {
        operations <- dbConfig.db.run(operationQuery.result) map { v => v.getOrElse(BigDecimal(0)) }
        refunds <- dbConfig.db.run(refundQuery.result) map { v => v.getOrElse(BigDecimal(0))}
        deposits <- dbConfig.db.run(depositQuery.result) map { v => v.getOrElse(BigDecimal(0)) }
      } yield CashState(operations - refunds + deposits)
    }
  }

  private def createValidationError(propertyName: String, error: String) =
    new PropertyValidationError(propertyName, error)

  private def checkAmount(amount: Option[BigDecimal]): Future[Option[PropertyValidationError]] = {
    getCurrentState map { state =>
      amount match {
        case Some(x) if x > state.amount =>
          Some(createValidationError("amount", "Нельзя снять больше, чем находится в кассе"))
        case Some(x) if x < 0 =>
          Some(createValidationError("amount", "Нельзя снять отрицательную сумму"))
        case None =>
          Some(createValidationError("amount", "Сумма должна быть указана"))
        case _ => None
      }
    }
  }

  override protected def validate(entity: E, performerId: Option[Int]): Future[ValidationResult] = {
    super.validate(entity, performerId) flatMap {
      case r: SuccessfulResult =>
        checkAmount(getAmount(entity)) map {
          case Some(e) => ValidationResult.propertyError(e)
          case None => ValidationResult.success()
        }
      case r => Future.successful(r)
    }
  }

  protected def getAmount(entity: E): Option[BigDecimal]
}
