package query

import java.sql.Timestamp
import javax.inject.Inject

import models.generated.Tables.RentFilter
import play.api.libs.json.{JsValue, Json}
import serialization.entity.Serialization
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.GetResult

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RentQuery @Inject() (dbConfig: DatabaseConfig[JdbcProfile]) extends SqlQuery(dbConfig) with Serialization {
  override val name: String = "q-rent"

  private implicit val format = Json.format[Result]

  private case class Result(
    id: Int,
    driver: Int,
    car: Int,
    deposit: BigDecimal,
    comment: Option[String],
    creator: Option[Int],
    creationDate: Option[Timestamp],
    editor: Option[Int],
    editDate: Option[Timestamp],
    status: String,
    carDisplayName: String,
    driverDisplayName: String)

  override protected def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    val filter = parseFilter(parameters)
    val sql = readSql(Map())

    implicit val getResult = GetResult(d => Result(d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<))
    fetchResult[Result](sql) map { items =>
      Json.toJson(items)
    }
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): RentFilter = {
    RentFilter()
  }
}
