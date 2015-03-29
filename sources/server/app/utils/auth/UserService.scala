package utils.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.entities.User
import utils.UserAlreadyExistsException
import utils.db.DbAccessor
import utils.db.repos.UsersRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UserService extends  DbAccessor {
  def hasUsers: Future[Boolean]
  def createUser(user: User): Future[User]
}

class UserServiceImpl(passwordHasher: PasswordHasher,
                      authInfoService: DelegableAuthInfoService,
                       identityService: IdentityService[User]) extends UserService {
  def hasUsers: Future[Boolean] = Future {
    withDb { session => UsersRepo.isEmpty(session) }
  }

  def createUser(user: User): Future[User] = {
    // тут мы должны сделать хеш пароля и сохранить его
    val loginInfo = LoginInfo(CredentialsProvider.ID, user.login)
    identityService.retrieve(loginInfo) flatMap {
      case None =>
        // пользователя с таким логином в БД нет
        // хешируем пароль
        val authInfo = passwordHasher.hash(user.password)
        Future {
          // сохраняем пользователя
          val id = withDb { session => UsersRepo.create(user.copy(password = ""))(session) }
          user.copy(id = id)
        } map { u =>
          // сохраняем хешированный пароль
          authInfoService.save(loginInfo, authInfo)
          u
        }
      case Some(u) =>
        // пользователь уже существует
        throw new UserAlreadyExistsException(user.login)
    }
  }
}


