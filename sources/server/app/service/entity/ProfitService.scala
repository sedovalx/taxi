package service.entity

import com.google.inject.Inject
import models.generated.Tables.{Profit, ProfitFilter, ProfitTable}
import repository.{RefundRepo, OperationRepo, ProfitRepo}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ProfitService extends EntityService[Profit, ProfitTable, ProfitRepo, ProfitFilter]{
  def getCurrentState: Future[BigDecimal]
}

class ProfitServiceImpl @Inject() (
  val repo: ProfitRepo,
  operationRepo: OperationRepo,
  refundRepo: RefundRepo,
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

  def getCurrentState: Future[BigDecimal] = {
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

      // расчетная сумма в кассе
      for {
        operations <- dbConfig.db.run(operationQuery.result) map { v => v.getOrElse(BigDecimal(0)) }
        refunds <- dbConfig.db.run(refundQuery.result) map { v => v.getOrElse(BigDecimal(0))}
      } yield operations - refunds
    }
  }

  private def checkAmount(entity: Profit): Future[Profit] = {
    getCurrentState map { amount =>
      entity.amount match {
        case Some(x) if x > amount => throw new RuntimeException(s"Невозможно изъять из кассы более $amount")
        case None => entity.copy(amount = Some(amount))
        case _ => entity
      }
    }
  }
}
