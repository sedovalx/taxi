package controllers.entities

import models.generated.Tables.{Account, AccountFilter, AccountTable}
import play.api.libs.json._
import repository.AccountRepo
import scaldi.Injector
import serialization.AccountSerializer
import service.AccountService

class AccountController(implicit inj: Injector)
  extends EntityController[Account, AccountTable, AccountRepo, AccountFilter, AccountSerializer]()(inj) {
  protected val entityService = inject [AccountService]
  override protected val serializer: AccountSerializer = inject [AccountSerializer]

  import serializer._

  protected val entitiesName: String = "users"
  protected val entityName: String = "user"

  override protected def beforeCreate(json: JsValue, entity: Account, identity: Account) = {
    val user = super.beforeCreate(json, entity, identity)
    require(user.passwordHash != null, "Пароль пользователя не должен быть пустым.")
    user
  }

  def currentUser = SecuredAction { request =>
    val user = request.identity
    Ok(makeJson("user", user))
  }
}



