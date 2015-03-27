package utils.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.entities.User
import models.repos.UsersRepo
import utils.db.DbAccessor

import scala.concurrent.Future

/**
 * Сервис хранения логинов
 */
class LoginInfoDAO extends IdentityService[User] with DbAccessor{
  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = Future {
    withDb { session =>
      UsersRepo.findByLogin(loginInfo.providerKey)(session)
    }
  }
}
