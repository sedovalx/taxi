package controllers

import models.repos.UsersRepo
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import utils.serialization.UserSerializer

/**
 * Контроллер операций над пользователями
 */
object UserController extends BaseController {

  // сериализация объектов пользователей в json
  implicit val userWrites = UserSerializer.writes

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = Action {
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
}
