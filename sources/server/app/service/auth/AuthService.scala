package service.auth

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.exceptions.NotAuthenticatedException
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import models.generated.Tables
import models.generated.Tables.SystemUser
import play.api.Logger
import play.api.libs.json._
import repository.SystemUserRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

  trait LoginInfoService extends IdentityService[SystemUser] {
    def retrieve(loginInfo: LoginInfo): Future[Option[SystemUser]]
  }


  class LoginInfoServiceImpl @Inject() (accountRepo: SystemUserRepo) extends LoginInfoService {
    override def retrieve(loginInfo: LoginInfo): Future[Option[SystemUser]] = {
        accountRepo.findByLogin(loginInfo.providerKey)
    }
  }

  class PasswordInfoServiceImpl @Inject() (accountRepo: SystemUserRepo) extends DelegableAuthInfoDAO[PasswordInfo] {
    implicit val passwordFormat = Json.format[PasswordInfo]

    def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
      // находим пользователя по логину
      findUser(loginInfo.providerKey) flatMap { user =>
        // сериализуем authInfo в строку
        val passwordInfoJson = Json.toJson(authInfo)
        // и сохраняем в БД
        accountRepo.update(user.copy(passwordHash = passwordInfoJson.toString())) map { _ => authInfo }
      }
    }

    override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
      // находим пользователя по логину
      findUser(loginInfo.providerKey) map { user =>
        val password = user.passwordHash
        // пробуем десериализовать пароль
        Json.parse(password).validate[PasswordInfo] match {
          case s: JsSuccess[PasswordInfo] => Some(s.get)
          case e: JsError =>
            Logger.error(s"Ошибка при десериализации хеша пароля: ${e.toString}")
            None
        }
      }
    }

    private def findUser(login: String): Future[Tables.SystemUser] = {
      accountRepo.findByLogin(login) map {
        case Some(u) => u
        case _ => throw new NotAuthenticatedException(s"Пользователь с логином $login не найден.")
      }
    }

    override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = save(loginInfo, authInfo)

    override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = save(loginInfo, authInfo)

    override def remove(loginInfo: LoginInfo): Future[Unit] = save(loginInfo, null).map(_ => Unit)
  }

