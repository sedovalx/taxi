package service.queries

import java.sql.Timestamp
import java.text.SimpleDateFormat

import models.entities.RentStatus.RentStatus
import play.api.Logger
import play.api.libs.json.{JsObject, Writes, JsValue, Json}
import repository.db.DbAccessor
import scaldi.{Injectable, Injector}
import utils.RunSqlException
import utils.extensions.DateUtils

import scala.slick.jdbc.{GetResult, StaticQuery => Q}


class CashierQuery(implicit injector: Injector) extends ConfQuery with Injectable with DbAccessor {
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

  override def execute(parameters: Map[String, Seq[String]]): JsValue = {
    val filter = parseFilter(parameters)
    val controlDate = DateUtils.iso8601Format.format(filter.date)
    val sql = readSql(Map("@control_date" -> s"'$controlDate'::timestamp"))

    implicit val getDataResult = GetResult(d => DataItem(d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<))
    val query = Q.queryNA[DataItem](sql)
    val data = try {
      withDb { implicit session => query.list }
    }
    catch {
      case e: Throwable =>
        Logger.error(s"Ошибка при выполнении запроса $name", e)
        throw new RunSqlException("Ошибка получения данных формы кассира", e)
    }
    JsObject(Seq("cashier-list" -> Json.toJson(data)))
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): Filter = Filter(date = DateUtils.now)

}

