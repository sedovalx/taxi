package controllers.entities

import java.sql.Date

import controllers.BaseController
import models.entities.User
import models.repos.UsersRepo
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers}
import scaldi.{Injectable, Injector}
import utils.serialization.UserSerializer._

/**
 * Контроллер операций над пользователями
 */
class UserController(implicit lnj: Injector) extends BaseController  with Injectable {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = Action { implicit request =>
    val users = withDb { session => UsersRepo.read(session) }
    val usersJson = makeJson("users", users)
    Ok(usersJson)
  }

  def getById(id: Long) = Action { implicit request =>
    val user = withDb { session => UsersRepo.findById(id)(session) }
    val userJson = makeJson("user", user)
    Ok(userJson)
  }

  def create = Action(BodyParsers.parse.json) { request =>
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

  def update(id: Long) = Action(BodyParsers.parse.json) { request =>
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

  def delete(id: Long) = Action { request =>
    val wasDeleted = withDb { session => UsersRepo.delete(id)(session) }
    if (wasDeleted)
      Ok(Json.parse("{}"))
    else
      NotFound(Json.obj("status" -> s"Пользователь с id=$id не найден"))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
