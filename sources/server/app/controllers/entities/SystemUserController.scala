package controllers.entities

import models.generated.Tables.{SystemUser, SystemUserFilter, SystemUserTable}
import play.api.libs.json._
import repository.SystemUserRepo
import scaldi.Injector
import serialization.SystemUserSerializer
import service.SystemUserService

class SystemUserController(implicit inj: Injector)
  extends EntityController[SystemUser, SystemUserTable, SystemUserRepo, SystemUserFilter, SystemUserSerializer]()(inj) {
  protected val entityService = inject [SystemUserService]
  override protected val serializer: SystemUserSerializer = inject [SystemUserSerializer]

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
    Ok(makeJson("user", user))
  }
}



