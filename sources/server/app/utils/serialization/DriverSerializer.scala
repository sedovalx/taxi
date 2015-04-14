package utils.serialization

import java.sql.Date
import java.text.SimpleDateFormat
import models.generated.Tables
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import models.generated.Tables.Driver

/**
 * Реализация сериализации/десереализации пользователей в json
 */
object DriverSerializer {
  val dateIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

/** implicit val enumReads = EnumSerializer.enumReads()
  * implicit val enumWrites = EnumSerializer.enumWrites
  */
  // десериализация
  implicit val driverReads = (
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "pass").read[String] and
      (JsPath \ "license").read[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "phone").read[String] and
      (JsPath \ "secPhone").read[String] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "address").read[String] and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Driver.apply _)

  // сериализация
  implicit val driverWrites =  new Writes[Driver] {
    def writes(driver: Driver) = Json.obj(
      "id" -> driver.id,
      "pass" -> driver.pass,
      "license" -> driver.license,
      "lastName" -> driver.lastName,
      "firstName" -> driver.firstName,
      "middleName" -> driver.middleName,
      "phone" -> driver.phone,
      "secPhone" -> driver.secPhone,
      "address" -> driver.address,
      "creationDate" -> driver.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> driver.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> driver.creatorId,
      "editor" -> driver.editorId,
      "comment" -> driver.comment
    )
  }
}
