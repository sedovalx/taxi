package service.entity

import com.google.inject.Inject
import models.generated.Tables.{Profit, ProfitFilter, ProfitTable}
import repository._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import utils.validation.ValidationError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class CashState(amount: BigDecimal)

trait ProfitService extends EntityService[Profit, ProfitTable, ProfitRepo, ProfitFilter] with CashService{
  def getCurrentState: Future[CashState]
}

class ProfitServiceImpl @Inject() (
  val repo: ProfitRepo,
  val profitRepo: ProfitRepo,
  val operationRepo: OperationRepo,
  val refundRepo: RefundRepo,
  val rentRepo: RentRepo,
  val dbConfig: DatabaseConfig[JdbcProfile]) extends ProfitService {

  override protected def validate(entity: Profit, producerId: Option[Int]): Future[Option[ValidationError]] = {
    super.validate(entity, producerId) flatMap {
      case Some(error) => Future.successful(Some(error))
      case None =>
        checkAmount(entity.amount) map {
          case Some(e) => Some(ValidationError.single("profit", e))
          case None => None
        }
    }
  }
}
