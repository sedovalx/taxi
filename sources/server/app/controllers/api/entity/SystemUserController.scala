package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{SystemUser, SystemUserFilter, SystemUserTable}
import play.api.i18n.MessagesApi
import play.api.libs.json._
import repository.SystemUserRepo
import serialization.entity.SystemUserSerializer
import service.entity.SystemUserService

class SystemUserController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: SystemUserService,
  val serializer: SystemUserSerializer)
  extends EntityController[SystemUser, SystemUserTable, SystemUserRepo, SystemUserFilter, SystemUserSerializer] {

  import serializer._

  protected val entitiesName: String = "users"
  protected val entityName: String = "user"

  override protected def beforeCreate(json: JsValue, entity: SystemUser, identity: SystemUser) = {
    val user = super.beforeCreate(json, entity, identity)
    require(user.passwordHash != null, "Пароль пользователя не должен быть пустым.")
    user
  }

  def currentUser = SecuredAction { request =>
    val user = request.identity
    logger.debug(s"Получен запрос на текущего пользователя: ${user.login}")
    Ok(makeJson("user", user))
  }
}



