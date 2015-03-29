package utils.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import utils.db.repos.UsersRepo
import play.api.libs.json._
import utils.db.DbAccessor
import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Сервис хранения паролей
 */
class PasswordInfoDAO extends DelegableAuthInfoDAO[PasswordInfo] with DbAccessor{

  implicit val passwordFormat = Json.format[PasswordInfo]

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = Future {
    // находим пользователя по логину
    val user = findUser(loginInfo.providerKey)

    // сериализуем authInfo в строку
    val passwordInfoJson = Json.toJson(authInfo)
    // и сохраняем в БД
    withDb { session => UsersRepo.update(user.copy(password = passwordInfoJson.toString()))(session) }

    authInfo
  }

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = Future {
    // находим пользователя по логину
    val user = findUser(loginInfo.providerKey)
    val password = user.password

    // пробуем десериализовать пароль
    Json.parse(password).validate[PasswordInfo] match {
      case s: JsSuccess[PasswordInfo] => Some(s.get)
      case e: JsError =>
        Logger.error(s"Ошибка при десериализации хеша пароля: ${e.toString}")
        None
    }
  }

  private def findUser(login: String) = {
    withDb { session => UsersRepo.findByLogin(login)(session) } match {
      case Some(u) => u
      case _ => throw new Exception(s"Пользователь с логином $login не найден. Пароль сохранен быть не может.")
    }
  }
}
