package query

import java.sql.Timestamp
import javax.inject.Inject

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import serialization.entity.Serialization
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.GetResult
import utils.{DateUtils, EntityJsonFormatException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RentHistoryQuery @Inject() (dbConfig: DatabaseConfig[JdbcProfile]) extends SqlQuery(dbConfig) with Serialization {

  private implicit val getResult = GetResult(d => HistoryRecord(d.<<, d.<<, d.<<, d.<<, d.<<, d.<<, d.<<))
  private implicit val operationRecordWrites = Json.writes[OperationRecord]
  private implicit val groupedWrites: Writes[Seq[((Int, Timestamp, String), Seq[OperationRecord])]] = new Writes[Seq[((Int, Timestamp, String), Seq[OperationRecord])]] {
    override def writes(o: Seq[((Int, Timestamp, String), Seq[OperationRecord])]): JsValue = {
      o.foldLeft(Json.arr())((sum, item) => {
        val statusId = item._1._1
        val statusTime = item._1._2
        val status = item._1._3
        sum :+ Json.obj(
          "id" -> statusId,
          "changeTime" -> Json.toJson(statusTime),
          "status" -> status.toString,
          "operations" -> Json.toJson(item._2)
        )
      })
    }
  }
  private implicit var filterReads: Reads[Filter] = (
    (JsPath \ "rent").read[String].map { v => v.toInt } and
      (JsPath \ "date").readNullable[String].map(_.map(v => DateUtils.valueOf(v)))
    )(Filter.apply _)

  override val name: String = "q-rent-history"

  case class Filter(rent: Int, date: Option[Timestamp] = Some(DateUtils.now))

  case class HistoryRecord(
    statusTime: Timestamp,
    status: String,
    statusId: Int,
    operationTime: Option[Timestamp],
    accountType: Option[String],
    amount: Option[BigDecimal],
    operationId: Option[Int]
    )

  case class OperationRecord(
    operationTime: Option[Timestamp],
    operationId: Option[Int],
    amount: Option[BigDecimal],
    accountType: Option[String]
    )

  override protected def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    val filter = parseFilter(parameters)
    val sql = readSql(Map(
      "@rentId" -> filter.rent.toString,
      "@controlTime" -> DateUtils.iso8601Format.format(filter.date.get)
    ))
    fetchResult[HistoryRecord](sql) map { history =>
      history
        .groupBy { h => (h.statusId, h.statusTime, h.status) }
        .toSeq.sortBy(h => h._1._2.getTime)
        .map { i => (
          i._1,
          i._2
            .filter { r => r.operationId.isDefined }
            .map { r => OperationRecord(r.operationTime, r.operationId, r.amount, r.accountType) }
            .sortBy { o => o.operationTime.map { t => t.getTime } }
        ) }
    } map { grouped =>
      Json.toJson(grouped)
    }
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): Filter = {
    val filterJson = Json.toJson(parameters map { i => (i._1, i._2.mkString) })
    Json.fromJson[Filter](filterJson) match {
      case e @ JsError(_) =>
        logger.error("Filter parsing error: \n" + e.toString)
        throw new EntityJsonFormatException("[Статус аренды]", "Ожидается поле rent с идентификатором аренды.")
      case JsSuccess(f, _) =>
        f.date match {
          case None => f.copy(date = Some(DateUtils.now))
          case _ => f
        }
    }
  }
}
