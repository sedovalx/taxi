package query

import java.sql.Timestamp

import play.api.libs.functional.syntax._
import play.api.libs.json._
import serialization.entity.Serialization
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import utils.{DateUtils, EntityJsonFormatException}

abstract class SqlRentQuery(dbConfig: DatabaseConfig[JdbcProfile]) extends SqlQuery(dbConfig) with Serialization {
  protected implicit var filterReads: Reads[Filter] = (
    (JsPath \ "rent").read[String].map { v => v.toInt } and
      (JsPath \ "date").readNullable[String].map(_.map(v => DateUtils.valueOf(v)))
    )(Filter.apply _)

  case class Filter(rent: Int, date: Option[Timestamp] = Some(DateUtils.now))

  protected def parseFilter(parameters: Map[String, Seq[String]]): Filter = {
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
