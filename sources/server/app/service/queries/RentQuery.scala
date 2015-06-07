package service.queries

import java.sql.Timestamp

import models.generated.Tables.RentFilter
import play.api.libs.json.{Json, JsValue}
import serialization.Serialization

import scala.slick.jdbc.GetResult

class RentQuery extends SqlQuery with Serialization {
  override val name: String = "q-rent"

  private implicit val format = Json.format[Result]

  private case class Result(
    id: Int,
    driverId: Int,
    carId: Int,
    deposit: BigDecimal,
    comment: Option[String],
    creatorId: Option[Int],
    creationDate: Option[Timestamp],
    editorId: Option[Int],
    editDate: Option[Timestamp],
    status: String,
    carDisplayName: String,
    driverDisplayName: String)

  override protected def doExecute(parameters: Map[String, Seq[String]]): JsValue = {
    val filter = parseFilter(parameters)
    val sql = readSql(Map())

    implicit val getResult = GetResult(d => Result(d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<))
    val data = fetchResult[Result](sql)
    Json.toJson(data)
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): RentFilter = {
    RentFilter()
  }
}
