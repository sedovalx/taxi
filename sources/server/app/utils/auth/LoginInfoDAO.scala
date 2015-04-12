package utils.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.generated.Tables.Account
import utils.db.DbAccessor
import utils.db.repo.AccountRepo
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Сервис хранения логинов
 */
class LoginInfoDAO extends IdentityService[Account] with DbAccessor {
  override def retrieve(loginInfo: LoginInfo): Future[Option[Account]] = Future {
    withDb { session =>
      AccountRepo.findByLogin(loginInfo.providerKey)(session)
    }
  }
}
