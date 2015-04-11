package utils.serialization

import java.sql.Date
import java.text.SimpleDateFormat

import models.entities.Role._
import models.entities.Role
import play.api.libs.json._
import play.api.libs.json.Reads._
import models.generated.Tables.Account

import play.api.libs.functional.syntax._


/**
 * Реализация сериализации/десереализации пользователей в json
 */
object UserSerializer {
  val dateIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  implicit val enumReads = EnumSerializer.enumReads(Role)
  implicit val enumWrites = EnumSerializer.enumWrites

  // десериализация
  implicit val userReads = (

      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "login").read(minLength[String](3)) and
      (JsPath \ "password").read(minLength[String](8)) and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "role").read[Role] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Account.apply _)

  // сериализация
  implicit val userWrites =  new Writes[Account] {
    def writes(user: Account) = Json.obj(
      "id" -> user.id,
      "login" -> user.login,
      "password" -> user.passwordHash,
      "lastName" -> user.lastName,
      "firstName" -> user.firstName,
      "middleName" -> user.middleName,
      "role" -> user.role.toString,
      "creationDate" -> user.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> user.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> user.creatorId,
      "editor" -> user.editorId,
      "comment" -> user.comment
    )
  }
}
