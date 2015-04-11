package utils.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.generated.Tables.Account
import utils.db.{Repositories, DbAccessor}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.slick.driver.JdbcProfile

/**
 * Сервис хранения логинов
 */
class LoginInfoDAO(val profile: JdbcProfile) extends IdentityService[Account] with DbAccessor with Repositories{
  override def retrieve(loginInfo: LoginInfo): Future[Option[Account]] = Future {
    withDb { session =>
      UsersRepo.findByLogin(loginInfo.providerKey)(session)
    }
  }
}
