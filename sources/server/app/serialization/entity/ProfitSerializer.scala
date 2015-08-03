package serialization.entity

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Profit, ProfitFilter}
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class ProfitSerializer extends Serializer[Profit, ProfitFilter] {
  override implicit val filterReads: Reads[Tables.ProfitFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "amount").readNullable[BigDecimal] and
    (JsPath \ "changeTime").readNullable[Timestamp] and
    (JsPath \ "creationDate").readNullable[Timestamp] and
    (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "editDate").readNullable[Timestamp] and
    (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "comment").readNullable[String]
  )(ProfitFilter.apply _)
  override implicit val reads: Reads[Tables.Profit] = (
  (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
    (JsPath \ "amount").readNullable(min[BigDecimal](0)) and
    (JsPath \ "changeTime").read[Timestamp] and
    (JsPath \ "creationDate").readNullable[Timestamp] and
    (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "editDate").readNullable[Timestamp] and
    (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "comment").readNullable[String]
  )(Profit.apply _)
  override implicit val writes: Writes[Tables.Profit] = new Writes[Profit] {
    override def writes(o: Tables.Profit): JsValue = Json.obj(
      "id" -> o.id.toString,
      "amount" -> o.amount.map { v => v.toString },
      "changeTime" -> dateIso8601Format.format(o.changeTime),
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
}
