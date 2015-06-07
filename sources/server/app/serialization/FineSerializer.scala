package serialization

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Fine, FineFilter}
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class FineSerializer extends Serializer[Fine, FineFilter]{
  override implicit val reads: Reads[Tables.Fine] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "changeTime").read[Timestamp] and
      (JsPath \ "amount").read(min[BigDecimal](0)) and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "rent").read[String].map { id => id.toInt } and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(Fine.apply _)
  override implicit val writes: Writes[Tables.Fine] = new Writes[Tables.Fine] {
    override def writes(o: Tables.Fine): JsValue = Json.obj(
      "id" -> o.id.toString,
      "rent" -> o.rentId.toString,
      "changeTime" -> dateIso8601Format.format(o.changeTime),
      "amount" -> o.amount.toString(),
      "description" -> o.description,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override implicit val filterReads: Reads[Tables.FineFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "fineDate").readNullable[Timestamp] and
      (JsPath \ "cost").readNullable[BigDecimal] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "rent").readNullable[String].map { id => id.map(_.toInt) } and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(FineFilter.apply _)
}
