package serialization.entity

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Refund, RefundFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

class RefundSerializer extends Serializer[Refund, RefundFilter] {
  override implicit val filterReads: Reads[Tables.RefundFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "amount").readNullable[BigDecimal] and
    (JsPath \ "changeTime").readNullable[Timestamp] and
    (JsPath \ "creationDate").readNullable[Timestamp] and
    (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "editDate").readNullable[Timestamp] and
    (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "comment").readNullable[String] and
    (JsPath \ "rent").readNullable[String].map { id => id.map(_.toInt) }
  )(RefundFilter.apply _)
  override implicit val reads: Reads[Tables.Refund] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
    (JsPath \ "amount").read(min[BigDecimal](0)) and
    (JsPath \ "changeTime").read[Timestamp] and
    (JsPath \ "creationDate").readNullable[Timestamp] and
    (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "editDate").readNullable[Timestamp] and
    (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
    (JsPath \ "comment").readNullable[String] and
    (JsPath \ "rent").read[String].map { id => id.toInt }
  )(Refund.apply _)
  override implicit val writes: Writes[Tables.Refund] = new Writes[Refund] {
    override def writes(o: Tables.Refund): JsValue = Json.obj(
      "id" -> o.id.toString,
      "amount" -> o.amount.toString,
      "changeTime" -> dateIso8601Format.format(o.changeTime),
      "rent" -> o.rentId.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
}
