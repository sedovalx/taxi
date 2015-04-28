package controllers.entities

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import controllers.filter.AccountFilter
import models.entities.Role
import models.generated.Tables.Account
import play.api.libs.json._
import play.api.mvc._
import scaldi.Injector
import service.AccountService
import utils.extensions.DateUtils._
import serialization.AccountSerializer._
import serialization.FormatJsError._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * Контроллер операций над пользователями
 */
class AccountController(implicit inj: Injector) extends BaseController with Silhouette[Account, JWTAuthenticator] {

  implicit val env = inject [Environment[Account, JWTAuthenticator]]
  implicit val accountService = inject[AccountService]


  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = SecuredAction.async { implicit request =>
    val filter = parseUserFilterFromQueryString(request)
    accountService.find(filter) map { users =>
      Ok(makeJson("users", users))
    }
  }

  def getById(id: Int) = SecuredAction.async { implicit request =>
    accountService.findById(id) map { user =>
      Ok(makeJson("user", user))
    }
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[Account] match {
      case JsSuccess(user, _) =>
        require(user.passwordHash != null, "Пароль пользователя не должен быть пустым.")
        accountService.create(user, Some(request.identity.id)) map { createdUser =>
          Ok(makeJson("user", createdUser))
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка создания пользователя", e.toString))
        }
      case err: JsError => Future.successful(UnprocessableEntity(Json.toJson(err)))
    }
  }

  def update(id: Int) = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[Account] match {
      case JsSuccess(user, _) =>
        // ember sends update data without id
        val toSave = user.copy(id = id)
        accountService.update(toSave, Some(request.identity.id)) map { saved =>
          val userJson = makeJson("user", saved)
          Ok(userJson)
        }
      case err: JsError => Future.successful(UnprocessableEntity(Json.toJson(err)))
    }
  }

  def delete(id: Int) = SecuredAction.async { request =>
    accountService.delete(id) map {
      case true => Ok(Json.obj())
      case false => NotFound(Response.bad(s"Пользователь с id=$id не найден"))
    }
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
}


