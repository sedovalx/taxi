package query

import java.sql.Timestamp

import models.entities.RentStatus._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

class CashierQuery extends SqlQuery {
  override val name: String = "q-cashier-list"

  private implicit val dataFormat = Json.format[DataItem]

  private case class Filter(
    car: Option[String] = None,
    driver: Option[String] = None,
    status: Option[RentStatus] = None,
    date: Timestamp
  )

  private case class DataItem(
    id: Int,
    rentId: Option[Int],
    driverId: Option[Int],
    car: String,
    driver: Option[String],
    presence: Option[Boolean],
    repairs: Option[BigDecimal],
    fines: Option[BigDecimal],
    payments: Option[BigDecimal],
    total: Option[BigDecimal],
    mileage: Option[BigDecimal],
    service: Option[BigDecimal],
    status: Option[String]
  )

  override protected def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    ???
  }
}
