package service

import javax.security.auth.login.AccountNotFoundException

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.{PasswordInfo, PasswordHasher}
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import controllers.filter.AccountFilter
import models.generated.Tables
import models.generated.Tables.{Account, AccountTable}
import play.api.libs.json.Json
import repository.db.DbAccessor
import repository.{AccountRepo, GenericCRUD}
import utils.extensions.DateUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AccountService extends DbAccessor with EntityService[Account, AccountTable, GenericCRUD[AccountTable, Account]] {
  def hasUsers: Future[Boolean]
  def find(userFilter : Option[AccountFilter]) : Future[List[Account]]
}

class AccountServiceImpl(accountRepo: AccountRepo,
                          passwordHasher: PasswordHasher,
                      authInfoService: DelegableAuthInfoService,
                       identityService: IdentityService[Account]) extends AccountService {

  override val repo = accountRepo

  implicit val passwordFormat = Json.format[PasswordInfo]

  override def setCreatorAndDate(entity: Account, creatorId: Option[Int])  =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Account, editorId: Option[Int])  =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Account, id: Int) = entity.copy(id = id)

  def hasUsers: Future[Boolean] = Future {
    withDb { session => accountRepo.isEmpty(session) }
  }

  override protected def beforeCreate(entity: Tables.Account, creatorId: Option[Int]): Future[Tables.Account] = {
    super.beforeCreate(entity, creatorId) map { toSave =>
      // перед сохранением нужно посчитать хеш пароля
      val authInfo = passwordHasher.hash(toSave.passwordHash)
      val passwordHash = Json.toJson(authInfo).toString()
      toSave.copy(passwordHash = passwordHash)
    }
  }

  override def find(userFilter: Option[AccountFilter]): Future[List[Account]] = Future {
    userFilter match {
      case Some(filter) => withDb { session => accountRepo.find(filter)(session) }
      case None => withDb { session => accountRepo.read(session) }
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


