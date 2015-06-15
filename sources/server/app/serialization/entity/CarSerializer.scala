package serialization.entity

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Car, CarFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

class CarSerializer extends Serializer[Car, CarFilter] {
  override implicit val filterReads: Reads[Tables.CarFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "regNumber").readNullable[String] and
      (JsPath \ "make").readNullable[String] and
      (JsPath \ "carModel").readNullable[String] and
      (JsPath \ "rate").readNullable[BigDecimal] and
      (JsPath \ "mileage").readNullable[BigDecimal] and
      (JsPath \ "service").readNullable[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(CarFilter.apply _)
  override implicit val reads: Reads[Tables.Car] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "regNumber").read(minLength[String](8)) and
      (JsPath \ "make").read[String] and
      (JsPath \ "carModel").read[String] and
      (JsPath \ "rate").read(min[BigDecimal](0)) and
      (JsPath \ "mileage").read(min[BigDecimal](0)) and
      (JsPath \ "service").readNullable(min[BigDecimal](0)) and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Car.apply _)
  override implicit val writes: Writes[Tables.Car] = new Writes[Car] {
    override def writes(o: Tables.Car) = Json.obj(
      "id" -> o.id,
      "regNumber" -> o.regNumber,
      "make" -> o.make,
      "carModel" -> o.model,
      "mileage" -> o.mileage,
      "service" -> o.service.map { s => s.toString() },
      "rate" -> o.rate.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
}
