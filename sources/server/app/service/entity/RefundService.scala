package service.entity

import com.google.inject.Inject
import models.generated.Tables.{Refund, RefundFilter, RefundTable}
import repository.{OperationRepo, ProfitRepo, RefundRepo, RentRepo}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import utils.validation.ValidationError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait RefundService extends EntityService[Refund, RefundTable, RefundRepo, RefundFilter] with CashService

class RefundServiceImpl @Inject() (
  val repo: RefundRepo,
  val profitRepo: ProfitRepo,
  val operationRepo: OperationRepo,
  val refundRepo: RefundRepo,
  val rentRepo: RentRepo,
  val dbConfig: DatabaseConfig[JdbcProfile]) extends RefundService {

  override protected def validate(entity: Refund, producerId: Option[Int]): Future[Option[ValidationError]] = {
    super.validate(entity, producerId) flatMap {
      case Some(error) => Future.successful(Some(error))
      case None =>
        checkAmount(Some(entity.amount)) map {
          case Some(e) => Some(ValidationError.single("refund", e))
          case None => None
        }
    }
  }
}
