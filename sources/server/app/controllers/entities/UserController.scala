package controllers.entities

import java.sql.Date

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.entities.User
import play.api.libs.json._
import play.api.mvc._
import utils.auth.{Environment, UserService}
import utils.db.repos.UsersRepo
import utils.extensions.SqlDate
import utils.serialization.UserSerializer._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * Контроллер операций над пользователями
 */
class UserController(val env: Environment,
                      userService: UserService)
  extends BaseController with Silhouette[User, JWTAuthenticator] {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = SecuredAction { implicit request =>
    val users = withDb { session => UsersRepo.read(session) }
    Ok(makeJson("users", users))
  }

  def getById(id: Long) = SecuredAction { implicit request =>
    val user = withDb { session => UsersRepo.findById(id)(session) }
    Ok(makeJson("user", user))
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => Future.successful(BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors)))),
      user => {
        //todo: перенести установку создателя в единую точку
        val toSave: User = user.copy(creatorId = Some(request.identity.id), creationDate = Some(SqlDate.now))
        userService.createUser(toSave) map { createdUser =>
          Ok(makeJson("user", createdUser))
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка создания пользователя", e.toString))
        }
      }
    )
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors))),
      user => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = user.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime)), editorId = Some(request.identity.id))
        withDbAction { session => UsersRepo.update(toSave)(session) }
        val userJson = makeJson("user", toSave)
        Ok(userJson)
      }
    )
  }

  def delete(id: Long) = SecuredAction { request =>
    val wasDeleted = withDb { session => UsersRepo.delete(id)(session) }
    if (wasDeleted) Ok(Json.parse("{}"))
    else NotFound(Response.bad(s"Пользователь с id=$id не найден"))
  }

  def currentUser = SecuredAction { request =>
    val user = request.identity
    Ok(makeJson("user", user))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]): JsValue = JsObject(Seq(prop -> Json.toJson(obj)))
}


