package serialization

import java.sql.Timestamp

import models.entities.RentStatus
import models.generated.Tables
import models.generated.Tables.{Rent, RentFilter}
import play.api.libs.json.{Json, JsPath, Reads, Writes}
import play.api.libs.functional.syntax._
import utils.serialization.EnumSerializer

class RentSerializer extends Serializer[Rent, RentFilter] {
  override implicit val reads: Reads[Tables.Rent] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "driver").read[String].map { id => id.toInt } and
      (JsPath \ "car").read[String].map { id => id.toInt } and
      (JsPath \ "deposit").read[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(Rent.apply _)
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

  implicit val enumReads = EnumSerializer.enumReads(RentStatus)
}
