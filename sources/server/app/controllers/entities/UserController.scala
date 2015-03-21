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
  def read = StackAction(AuthorityKey -> Set(Role.Administrator)) { implicit request =>
    val usersJson = withDb { session =>
      // получаем всех пользователей из БД
      val users = UsersRepo.read(session)
      // преобразуем их в json. тут неявно используется сериалайзер выше
      JsObject(Seq(
        "users" -> Json.toJson(users)
      ))
    }
    Ok(usersJson)
  }

  def getById(id: Long) = StackAction(AuthorityKey -> Set(Role.Administrator)) { implicit request =>
    val userJson = withDb { session =>
      val user = UsersRepo.findById(id)(session)
      JsObject(Seq(
        "user" -> Json.toJson(user)
      ))
    }
    Ok(userJson)
  }

  def create = StackAction(BodyParsers.parse.json, AuthorityKey -> Set(Role.Administrator)) { request =>
    val json = (request.body \ "user")
    json.validate[User].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      user => {
        val toSave: User = user.copy(creatorId = Some(loggedIn(request).id))
        val id = withDb { session => UsersRepo.create(toSave)(session) }
        val toSend = toSave.copy(id = id)
        val userJson = JsObject(Seq(
          "user" -> Json.toJson(toSend)
        ))
        Ok(userJson)
      }
    )
  }
}
