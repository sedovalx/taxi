package service.entity

import repository.{OperationRepo, ProfitRepo, RefundRepo, RentRepo}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import utils.validation.PropertyValidationError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CashService {
  val profitRepo: ProfitRepo
  val operationRepo: OperationRepo
  val refundRepo: RefundRepo
  val rentRepo: RentRepo
  val dbConfig: DatabaseConfig[JdbcProfile]

  import dbConfig.driver.api._

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

  private def createValidationError(propertyName: String, error: String): Option[PropertyValidationError] =
    Some(PropertyValidationError.single(propertyName, error))

  protected def checkAmount(amount: Option[BigDecimal]): Future[Option[PropertyValidationError]] = {
    getCurrentState map { state =>
      amount match {
        case Some(x) if x > state.amount =>
          createValidationError("amount", "Нельзя снять больше, чем находится в кассе")
        case Some(x) if x < 0 =>
          createValidationError("amount", "Нельзя снять отрицательную сумму")
        case None =>
          createValidationError("amount", "Сумма должна быть указана")
        case _ => None
      }
    }
  }
}
