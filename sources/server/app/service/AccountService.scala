package service

import javax.security.auth.login.AccountNotFoundException

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.{PasswordHasher, PasswordInfo}
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables
import models.generated.Tables.{Account, AccountFilter, AccountTable}
import play.api.libs.json.Json
import repository.AccountRepo
import repository.db.DbAccessor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AccountService extends DbAccessor with EntityService[Account, AccountTable, AccountRepo, AccountFilter] {
  def hasUsers: Future[Boolean]
}

class AccountServiceImpl(
                          val repo: AccountRepo,
                          passwordHasher: PasswordHasher,
                          authInfoService: DelegableAuthInfoService,
                          identityService: IdentityService[Account]) extends AccountService {

  implicit val passwordFormat = Json.format[PasswordInfo]

  def hasUsers: Future[Boolean] = Future {
    withDb { session => repo.isEmpty(session) }
  }

  override protected def beforeCreate(entity: Tables.Account, creatorId: Option[Int]): Future[Tables.Account] = {
    super.beforeCreate(entity, creatorId) map { toSave =>
      // перед сохранением нужно посчитать хеш пароля
      val authInfo = passwordHasher.hash(toSave.passwordHash)
      val passwordHash = Json.toJson(authInfo).toString()
      toSave.copy(passwordHash = passwordHash)
    }
  }

  override protected def beforeUpdate(entity: Tables.Account, editorId: Option[Int]): Future[Tables.Account] = {
    super.beforeUpdate(entity, editorId) flatMap { toSave =>
      // если поменялся пароль, то нужно пересчитать хеш
      if (toSave.passwordHash != null) {
        val authInfo = passwordHasher.hash(toSave.passwordHash)
        val passwordHash = Json.toJson(authInfo).toString()
        Future.successful(toSave.copy(passwordHash = passwordHash))
      } else {
        findOrThrow(entity.id) map { existing =>
          toSave.copy(passwordHash = existing.passwordHash)
        }
      }
    }
  }

  private def findOrThrow(id: Int) =
    this.findById(id) map {
      case Some(u) => u
      case None => throw new AccountNotFoundException(s"Account id=$id")
    }
}


