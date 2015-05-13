package controllers.entities

import java.sql.Timestamp

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.entities.Role
import models.entities.Role._
import models.generated.Tables
import models.generated.Tables.{Account, AccountTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.AccountRepo
import scaldi.Injector
import service.AccountService
import utils.serialization.EnumSerializer

class AccountController(implicit inj: Injector) extends EntityController[Account, AccountTable, AccountRepo]()(inj) {
  protected val entityService = inject [AccountService]

  protected val entitiesName: String = "users"
  protected val entityName: String = "user"

  private implicit val enumReads = EnumSerializer.enumReads(Role)
  private implicit val enumWrites = EnumSerializer.enumWrites

  protected def copyEntityWithId(entity: Account, id: Int) = entity.copy(id = id)

  override protected def beforeCreate(json: JsValue, entity: Account, identity: Account) = {
    val user = super.beforeCreate(json, entity, identity)
    require(user.passwordHash != null, "Пароль пользователя не должен быть пустым.")
    user
  }

  protected implicit val reads: Reads[Tables.Account] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "login").read(minLength[String](3)) and
      (JsPath \ "password").readNullable(minLength[String](8)).map(o => o.orNull) and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "role").read[Role] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Account.apply _)

  protected implicit val writes: Writes[Tables.Account] = new Writes[Account] {
    def writes(o: Account) = Json.obj(
      "id" -> o.id.toString,
      "login" -> o.login,
      "password" -> JsNull,
      "lastName" -> o.lastName,
      "firstName" -> o.firstName,
      "middleName" -> o.middleName,
      "role" -> o.role.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }

  def currentUser = SecuredAction { request =>
    val user = request.identity
    Ok(makeJson("user", user))
  }
}



