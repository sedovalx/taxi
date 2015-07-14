package query

import java.sql.Timestamp
import javax.inject.Inject

import models.entities.RentStatus
import models.entities.RentStatus._
import play.api.libs.json.{JsSuccess, JsError, JsValue, Json}
import serialization.EnumSerializer
import serialization.entity.Serialization
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.GetResult
import utils.DateUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CashierQuery @Inject()(dbConfig: DatabaseConfig[JdbcProfile]) extends SqlQuery(dbConfig) with Serialization {
  import dbConfig.driver.api._

  override val name: String = "q-cashier-list"

  private implicit val dataFormat = Json.format[DataItem]
  private implicit val getResult = GetResult[DataItem] { d => new DataItem(d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<)}
  private implicit val enumReads = EnumSerializer.enumReads(RentStatus)
  private implicit val filterReads = Json.reads[Filter]

  private case class Filter(
    car: Option[String] = None,
    driver: Option[String] = None,
    status: Option[RentStatus] = None,
    date: Option[Timestamp] = Some(DateUtils.now)
  )

  private case class DataItem(
    id: Int,
    rentId: Option[Int],
    rentCreationDate: Option[Timestamp],
    driverId: Option[Int],
    car: String,
    driver: Option[String],
    presence: Option[Boolean],
    payments: Option[BigDecimal],
    fines: Option[BigDecimal],
    repairs: Option[BigDecimal],
    total: Option[BigDecimal],
    mileage: Option[BigDecimal],
    service: Option[BigDecimal],
    status: Option[String]
  )

  override protected def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    val filter = parseFilter(parameters)
    val whereClause = buildWhereClause(filter)
    val itemsQuery: DBIO[Seq[DataItem]] =
      sql"""
           select
              car_id,
              rent_id,
              rent_creation_date,
              driver_id,
              car,
              driver,
              presence,
              payments,
              fines,
              repairs,
              total,
              mileage,
              service,
              status
           from func_cashier(${filter.date})
           where #$whereClause
           order by status, driver""".as[DataItem]
    dbConfig.db.run(itemsQuery) map { items =>
      Json.obj("cashier-lists" -> Json.toJson(items))
    }
  }

  private def buildWhereClause(filter: Filter): String = {
    var whereClause = "1=1"
    if (filter.car.isDefined){
      whereClause += s" and car ilike '%${filter.car.get}%'"
    }
    if (filter.driver.isDefined){
      whereClause += s" and driver ilike '%${filter.driver.get}%'"
    }
    whereClause
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): Filter = {
    val filterJson = Json.toJson(parameters map { i => (i._1, i._2.mkString) })
    Json.fromJson[Filter](filterJson) match {
      case e @ JsError(_) =>
        logger.warn("Filter parsing error: \n" + e.toString)
        Filter()
      case JsSuccess(f, _) =>
        f.date match {
          case None => f.copy(date = Some(DateUtils.now))
          case _ => f
        }
    }
  }
}
