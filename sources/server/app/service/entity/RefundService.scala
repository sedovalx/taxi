package service.entity

import com.google.inject.Inject
import models.generated.Tables
import models.generated.Tables.{Refund, RefundFilter, RefundTable}
import repository.{OperationRepo, ProfitRepo, RefundRepo, RentRepo}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait RefundService extends CashService[Refund, RefundTable, RefundRepo, RefundFilter]

class RefundServiceImpl @Inject() (
  val repo: RefundRepo,
  val profitRepo: ProfitRepo,
  val operationRepo: OperationRepo,
  val refundRepo: RefundRepo,
  val rentRepo: RentRepo,
  val dbConfig: DatabaseConfig[JdbcProfile]) extends RefundService {

  override protected def getAmount(entity: Tables.Refund): Option[BigDecimal] = Some(entity.amount)
}
