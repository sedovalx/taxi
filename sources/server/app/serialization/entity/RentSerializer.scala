package serialization.entity

import java.sql.Timestamp

import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Rent, RentFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}
import serialization.EnumSerializer

class RentSerializer extends Serializer[Rent, RentFilter] {
  implicit val enumReads = EnumSerializer.enumReads(RentStatus)

  override implicit val reads: Reads[Tables.Rent] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "driver").read[String].map { id => id.toInt } and
      (JsPath \ "car").read[String].map { id => id.toInt } and
      (JsPath \ "deposit").read[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "status").read[RentStatus]
    )((a1, a2, a3, a4, a5, a6, a7, a8, a9, _) => Rent.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9))

  override implicit val writes: Writes[Tables.Rent] = new Writes[Rent] {
    override def writes(o: Tables.Rent) = Json.obj(
      "id" -> o.id.toString,
      "driver" -> o.driverId.toString,
      "car" -> o.carId.toString,
      "deposit" -> o.deposit.toString(),
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }

  override implicit val filterReads: Reads[Tables.RentFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "driver").readNullable[String].map { id => id.map(_.toInt) } and
      (JsPath \ "car").readNullable[String].map { id => id.map(_.toInt) } and
      (JsPath \ "deposit").readNullable[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(RentFilter.apply _)
}
