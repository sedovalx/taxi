package controllers.entities

import javax.security.auth.login.AccountNotFoundException

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import controllers.filter.AccountFilter
import models.entities.Role
import models.generated.Tables.Account
import play.api.libs.json._
import play.api.mvc._
import utils.auth.{AccountService, Environment}
import utils.db.repo.AccountRepo
import utils.extensions.DateUtils
import utils.extensions.DateUtils._
import utils.serialization.AccountSerializer._
import utils.serialization.FormatJsError._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * Контроллер операций над пользователями
 */
class AccountController(val env: Environment,
                      userService: AccountService)
  extends BaseController with Silhouette[Account, JWTAuthenticator] {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = SecuredAction { implicit request =>
    val userFilterOpt = parseUserFilterFromQueryString(request)
    var users = List[Account]()
    withDb { session =>
      userFilterOpt match {
        case Some(userFilter: AccountFilter) => users = AccountRepo.find(userFilter)(session)
        case _ => users = AccountRepo.read(session)
    }
    }
    //val users = withDb { session => UsersRepo.read(session) }
    Ok(makeJson("users", users))
  }

  def getById(id: Int) = SecuredAction { implicit request =>
    val user = withDb { session => AccountRepo.findById(id)(session) }
    Ok(makeJson("user", user))
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[Account] match {
      case JsSuccess(user, _) =>
        require(user.passwordHash != null, "Пароль пользователя не должен быть пустым.")
        val toSave: Account = user.copy(creatorId = Some(request.identity.id), creationDate = Some(DateUtils.now))
        userService.createAccount(toSave) map { createdUser =>
          Ok(makeJson("user", createdUser))
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка создания пользователя", e.toString))
        }
      case err@JsError(_) => Future(UnprocessableEntity(Json.toJson(err)))
    }
  }

  def update(id: Int) = SecuredAction(BodyParsers.parse.json) { request =>
    val existingUser = findOrThrow(id)
    val json = request.body \ "user"
    json.validate[Account] match {
      case JsSuccess(user, _) =>
        // todo: перенести установку времени редактирования в единую точку
        val toSave = if (user.passwordHash == null) {
          // пароль не был изменен пользователем
          user.copy(passwordHash = existingUser.passwordHash)
        } else {
          // пароль поменялся - нужно обновить хеш
          user
        }.copy(id = id, editDate = Some(DateUtils.now), editorId = Some(request.identity.id))

        withDbAction { session => AccountRepo.update(toSave)(session) }
        val userJson = makeJson("user", toSave)
        Ok(userJson)
      case err@JsError(_) => UnprocessableEntity(Json.toJson(err))
    }
  }

  def delete(id: Int) = SecuredAction { request =>
    val wasDeleted = withDb { session => AccountRepo.delete(id)(session) }
    if (wasDeleted) Ok(Json.parse("{}"))
    else NotFound(Response.bad(s"Пользователь с id=$id не найден"))
  }

  def currentUser = SecuredAction { request =>
    val user = request.identity
    Ok(makeJson("user", user))
  }


  def parseUserFilterFromQueryString(implicit request:RequestHeader) : Option[AccountFilter] = {
    val query: Map[String, String] = request.queryString.map { case (k, v) => k -> v.mkString }
    val login = query.get("login")
    val lastName = query.get("lastName")
    val firstName = query.get("firstName")
    val middleName = query.get("middleName")
    val role = Role.toRole(query.get("role"))
    //TODO: уточнить формат даты
    val createDate = stringToDate ( query.get("createDate") )

    val filter = new AccountFilter(login, lastName, firstName, middleName, role, createDate)

    //TODO: вынести в utils?
    val fieldList = filter.productIterator.toList.collect({ case Some(x) => x })
    val hasAny = fieldList.length > 0

    hasAny match {
      case true => Some(filter)
      case _ => None
    }
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]): JsValue = JsObject(Seq(prop -> Json.toJson(obj)))

  private def findOrThrow(id: Int) =
    withDb { session => AccountRepo.findById(id) } match {
      case Some(u) => u
      case None => throw new AccountNotFoundException(s"Account id=$id")
    }
}


