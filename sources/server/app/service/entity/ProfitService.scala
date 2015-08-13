package service.entity

import com.google.inject.Inject
import models.generated.Tables
import models.generated.Tables.{Profit, ProfitFilter, ProfitTable}
import repository._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile



trait ProfitService extends CashService[Profit, ProfitTable, ProfitRepo, ProfitFilter]

class ProfitServiceImpl @Inject() (
  val repo: ProfitRepo,
  val profitRepo: ProfitRepo,
  val operationRepo: OperationRepo,
  val refundRepo: RefundRepo,
  val rentRepo: RentRepo,
  val dbConfig: DatabaseConfig[JdbcProfile]) extends ProfitService {

  override protected def getAmount(entity: Tables.Profit): Option[BigDecimal] = entity.amount
}
