package controllers.entities

import java.sql.Date

import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.entities.User
import models.repos.UsersRepo
import play.api.libs.json._
import play.api.mvc._
import utils.serialization.UserSerializer._

/**
 * Контроллер операций над пользователями
 */
class UserController(implicit val env: Environment[User, JWTAuthenticator]) extends BaseController with Silhouette[User, JWTAuthenticator] {

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

  def create = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      user => {
        //todo: перенести установку создателя в единую точку
//        val toSave: User = user.copy(creatorId = Some(loggedIn(request).id))
        val toSave = user.copy()
        val id = withDb { session => UsersRepo.create(toSave)(session) }
        val toSend = toSave.copy(id = id)
        val userJson = makeJson("user", toSend)
        Ok(userJson)
      }
    )
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      user => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = user.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime))/*, editorId = Some(loggedIn(request).id)*/)
        withDbAction { session => UsersRepo.update(toSave)(session) }
        val userJson = makeJson("user", toSave)
        Ok(userJson)
      }
    )
  }

  def delete(id: Long) = SecuredAction { request =>
    val wasDeleted = withDb { session => UsersRepo.delete(id)(session) }
    if (wasDeleted) Ok(Json.parse("{}"))
    else NotFound(Json.obj("status" -> s"Пользователь с id=$id не найден"))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
