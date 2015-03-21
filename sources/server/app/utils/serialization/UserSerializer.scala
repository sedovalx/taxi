package utils.serialization

import java.sql.Date
import java.text.SimpleDateFormat

import models.entities.User
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
 * Реализация сериализации/десереализации пользователей в json
 */
object UserSerializer {
  val dateIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  // сериализация
  val writes = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "login" -> user.login,
      "password" -> "*******",
      "lastName" -> user.lastName,
      "firstName" -> user.firstName,
      "middleName" -> user.middleName,
      "role" -> user.role.toString,
      "creationDate" -> dateIso8601Format.format(user.creationDate),
      "editDate" -> user.editDate,
      "creator" -> user.creatorId,
      "editor" -> user.editorId
    )
  }

//  // десериализация
//  implicit val userReads: Reads[User] = (
//    (JsPath \ "user" \ "id").read[Long] and
//      (JsPath \ "user" \ "lastName").readNullable[String] and
//      (JsPath \ "user" \ "firstName").readNullable[String] and
//      (JsPath \ "user" \ "middleName").readNullable[String] and
//      (JsPath \ "user" \ "login").read[String] and
//      (JsPath \ "user" \ "password").read[String] and
//      (JsPath \ "user" \ "creationDate").readNullable[Date] and
//    )(User.apply _)
}
