package service.queries

import java.sql.Timestamp

import models.entities.RentStatus.RentStatus
import play.api.libs.json.{JsObject, JsValue, Json}
import utils.extensions.DateUtils

import scala.slick.jdbc.GetResult

class CashierQuery extends SqlQuery {
  override val name: String = "q-cashier-list"

  private implicit val dataFormat = Json.format[DataItem]

  private case class Filter(car: Option[String] = None, driver: Option[String] = None, status: Option[RentStatus] = None, date: Timestamp)

  private case class DataItem(
     id: Int,
     car: String,
     rentId: Option[Int],
     driver: Option[String],
     presence: Option[Boolean],
     repairs: Option[BigDecimal],
     fines: Option[BigDecimal],
     balance: Option[BigDecimal],
     mileage: Option[BigDecimal],
     service: Option[BigDecimal],
     status: Option[String]
  )

  override def doExecute(parameters: Map[String, Seq[String]]): JsValue = {
    val filter = parseFilter(parameters)
    val controlDate = DateUtils.iso8601Format.format(filter.date)
    val sql = readSql(Map("@control_date" -> s"'$controlDate'::timestamp"))

    implicit val getDataResult = GetResult(d => DataItem(d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<))
    val data = fetchResult[DataItem](sql)
    JsObject(Seq("cashier-list" -> Json.toJson(data)))
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): Filter = Filter(date = DateUtils.now)
}

