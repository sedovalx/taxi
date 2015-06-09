package service.queries

import java.sql.Timestamp

import play.api.libs.json.{Json, JsValue}
import serialization.Serialization

import scala.slick.jdbc.GetResult

class IvanTestQuery extends SqlQuery with Serialization {

  private case class Result(start: Timestamp, end: Option[Timestamp])
  private implicit val format = Json.format[Result]

  override protected def doExecute(parameters: Map[String, Seq[String]]): JsValue = {
    val sql = readSql(Map())

    implicit val getResult = GetResult(d => Result(d.<<, d.<<))
    val data = fetchResult[Result](sql)
    Json.toJson(data)
  }

  override val name: String = "q-ivan-test"
}
