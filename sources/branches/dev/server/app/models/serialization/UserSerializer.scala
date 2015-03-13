package models.serialization

import java.text.SimpleDateFormat

import models.entities.User
import play.api.libs.json.{Json, Writes}

object UserSerializer {
  val writes = new Writes[User] {
    val dateIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "lastName" -> user.lastName,
      "firstName" -> user.firstName,
      "middleName" -> user.middleName,
      "role" -> user.role.toString,
      "creationDate" -> dateIso8601Format.format(user.creationDate),
      "editDate" -> user.editDate,
      "creatorId" -> user.creatorId,
      "editorId" -> user.editorId
    )
  }
}
