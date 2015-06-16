package query

import java.sql.Timestamp
import javax.inject.Inject

import models.entities.RentStatus._
import play.api.libs.json.{JsValue, Json}
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

  private case class Filter(
    car: Option[String] = None,
    driver: Option[String] = None,
    status: Option[RentStatus] = None,
    date: Timestamp
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
    val itemsQuery: DBIO[Seq[DataItem]] =
      sql"""
           select *
           from func_cashier(${filter.date})
           order by rent_creation_date, car""".as[DataItem]
    dbConfig.db.run(itemsQuery) map { items =>
      Json.obj("cashier-lists" -> Json.toJson(items))
    }
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): Filter = {
    Filter(date = DateUtils.now)
  }
}
