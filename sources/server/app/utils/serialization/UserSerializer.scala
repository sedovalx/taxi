package utils.serialization

import java.sql.Date
import java.text.SimpleDateFormat

import models.entities.Role._
import models.entities.{Role, User}
import play.api.libs.json._
import play.api.libs.json.Reads._
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
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toLong case None => 0 } and
      (JsPath \ "login").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "role").read[Role] and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toLong) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toLong) }
    )(User.apply _)

  // сериализация
  implicit val userWrites =  new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "login" -> user.login,
      "password" -> user.password,
      "lastName" -> user.lastName,
      "firstName" -> user.firstName,
      "middleName" -> user.middleName,
      "role" -> user.role.toString,
      "creationDate" -> user.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> user.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> user.creatorId,
      "editor" -> user.editorId
    )
  }
}
