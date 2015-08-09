package service.entity

import com.google.inject.Inject
import models.generated.Tables.{Profit, ProfitFilter, ProfitTable}
import repository._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class CashState(amount: BigDecimal)

trait ProfitService extends EntityService[Profit, ProfitTable, ProfitRepo, ProfitFilter]{
  def getCurrentState: Future[CashState]
}

class ProfitServiceImpl @Inject() (
  val repo: ProfitRepo,
  operationRepo: OperationRepo,
  refundRepo: RefundRepo,
  rentRepo: RentRepo,
  dbConfig: DatabaseConfig[JdbcProfile]) extends ProfitService {

  import dbConfig.driver.api._

  override protected def beforeCreate(entity: Profit, creatorId: Option[Int]): Future[Profit] = {
    super.beforeCreate(entity, creatorId) flatMap { entity =>
      checkAmount(entity)
    }
  }

  override protected def beforeUpdate(entity: Profit, editorId: Option[Int]): Future[Profit] = {
    super.beforeUpdate(entity, editorId) flatMap { entity =>
      checkAmount(entity)
    }
  }

  def getCurrentState: Future[CashState] = {
    val zero = BigDecimal(0)
    // дата последнего снятия кассы
    val lastProfitQuery = repo.tableQuery.sortBy(p => p.changeTime.desc).take(1).map(p => p.changeTime)
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
        deposites <- dbConfig.db.run(depositQuery.result) map { v => v.getOrElse(BigDecimal(0)) }
      } yield CashState(operations - refunds + deposites)
    }
  }

  private def checkAmount(entity: Profit): Future[Profit] = {
    getCurrentState map { state =>
      entity.amount match {
        case Some(x) if x > state.amount => throw new RuntimeException(s"Невозможно изъять из кассы более ${state.amount}")
        case None => entity.copy(amount = Some(state.amount))
        case _ => entity
      }
    }
  }
}
