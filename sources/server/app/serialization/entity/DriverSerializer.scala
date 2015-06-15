package serialization.entity

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Driver, DriverFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

class DriverSerializer extends Serializer[Driver, DriverFilter] {
  override implicit val filterReads: Reads[Tables.DriverFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "pass").readNullable[String] and
      (JsPath \ "license").readNullable[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "phone").readNullable[String] and
      (JsPath \ "secPhone").readNullable[String] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "address").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(DriverFilter.apply _)
  override implicit val reads: Reads[Tables.Driver] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "pass").read(minLength[String](10)) and
      (JsPath \ "license").read(minLength[String](10)) and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "phone").read[String] and
      (JsPath \ "secPhone").read[String] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "address").read[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Driver.apply _)
  override implicit val writes: Writes[Tables.Driver] = new Writes[Driver] {
    def writes(o: Driver) = Json.obj(
      "id" -> o.id.toString,
      "pass" -> o.pass,
      "license" -> o.license,
      "lastName" -> o.lastName,
      "firstName" -> o.firstName,
      "middleName" -> o.middleName,
      "phone" -> o.phone,
      "secPhone" -> o.secPhone,
      "address" -> o.address,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
}
