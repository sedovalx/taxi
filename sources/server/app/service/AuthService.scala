package service


import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import repository.AccountRepo
import repository.db.DbAccessor
import models.generated.Tables.Account

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.exceptions.NotAuthenticatedException
import com.mohiva.play.silhouette.api.util.PasswordInfo
import play.api.Logger
import play.api.libs.json._
import scaldi.{Injectable, Injector}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by ipopkov on 14/04/15.
 */

  trait LoginInfoService extends IdentityService[Account] with DbAccessor {
    def retrieve(loginInfo: LoginInfo): Future[Option[Account]]
  }


  class LoginInfoServiceImpl(implicit inj: Injector) extends LoginInfoService with Injectable{

    val accountRepo = inject[AccountRepo]


    override def retrieve(loginInfo: LoginInfo): Future[Option[Account]] = Future {
      withDb { session =>
        accountRepo.findByLogin(loginInfo.providerKey)(session)
      }
    }
  }


  trait PasswordInfoService extends DelegableAuthInfoDAO[PasswordInfo] with DbAccessor {
    def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo]
    def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]]
  }

  class PasswordInfoServiceImpl  extends PasswordInfoService  with Injectable{
    val accountRepo = inject[AccountRepo]

    implicit val passwordFormat = Json.format[PasswordInfo]

    override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = Future {
      // находим пользователя по логину
      val user = findUser(loginInfo.providerKey)

      // сериализуем authInfo в строку
      val passwordInfoJson = Json.toJson(authInfo)
      // и сохраняем в БД
      withDb { session => accountRepo.update(user.copy(passwordHash = passwordInfoJson.toString()))(session) }
      authInfo
    }

    override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = Future {
      // находим пользователя по логину
      val user = findUser(loginInfo.providerKey)
      val password = user.passwordHash

      // пробуем десериализовать пароль
      println(user.toString)
      Json.parse(password).validate[PasswordInfo] match {
        case s: JsSuccess[PasswordInfo] => Some(s.get)
        case e: JsError =>
          Logger.error(s"Ошибка при десериализации хеша пароля: ${e.toString}")
          None
      }
    }

    private def findUser(login: String) = {
      withDb { session => accountRepo.findByLogin(login)(session) } match {
        case Some(u) => u
        case _ => throw new NotAuthenticatedException(s"Пользователь с логином $login не найден.")
      }
    }
  }

