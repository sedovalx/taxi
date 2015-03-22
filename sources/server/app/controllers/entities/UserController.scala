package controllers.entities

import java.sql.Date

import controllers.BaseController
import controllers.auth.AuthConfigImpl
import jp.t2v.lab.play2.auth.AuthElement
import models.entities.Role
import models.repos.UsersRepo
import play.api.libs.json._
import play.api.mvc.BodyParsers
import utils.serialization.UserSerializer._

/**
 * Контроллер операций над пользователями
 */
object UserController extends BaseController  with AuthElement with AuthConfigImpl {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = StackAction(AuthorityKey -> Set()) { implicit request =>
    val users = withDb { session => UsersRepo.read(session) }
    val usersJson = makeJson("users", users)
    Ok(usersJson)
  }

  def getById(id: Long) = StackAction(AuthorityKey -> Set()) { implicit request =>
    val user = withDb { session => UsersRepo.findById(id)(session) }
    val userJson = makeJson("user", user)
    Ok(userJson)
  }

  def create = StackAction(BodyParsers.parse.json, AuthorityKey -> Set()) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      user => {
        //todo: перенести установку создателя в единую точку
        val toSave: User = user.copy(creatorId = Some(loggedIn(request).id))
        val id = withDb { session => UsersRepo.create(toSave)(session) }
        val toSend = toSave.copy(id = id)
        val userJson = makeJson("user", toSend)
        Ok(userJson)
      }
    )
  }

  def update(id: Long) = StackAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      user => {
        if (id != user.id)
          throw new Exception("Переданные данные не соответствуют маршруту.")
        // todo: перенести установку времени редактирования в единую точку
        val toSave = user.copy(editDate = Some(new Date(new java.util.Date().getTime)), editorId = Some(loggedIn(request).id))
        withDbAction { session => UsersRepo.update(toSave)(session) }
        val userJson = makeJson("user", toSave)
        Ok(userJson)
      }
    )
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
