package service

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import controllers.filter.AccountFilter
import models.generated.Tables
import models.generated.Tables.{AccountTable, Account}
import repository.{GenericCRUD, AccountRepo}
import repository.db.DbAccessor
import utils.AccountAlreadyExistsException
import utils.extensions.DateUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AccountService extends DbAccessor with EntityService[Account, AccountTable, GenericCRUD[AccountTable, Account]] {
  def hasUsers: Future[Boolean]
  def createAccount(user: Account): Future[Account]
  def find(userFilter : AccountFilter) : List[Account]
}

class AccountServiceImpl(accountRepo: AccountRepo,
                          passwordHasher: PasswordHasher,
                      authInfoService: DelegableAuthInfoService,
                       identityService: IdentityService[Account]) extends AccountService {

  override val repo = accountRepo

  override def setCreatorAndDate(entity: Account, creatorId: Int)  =
    entity.copy(creatorId = Some(creatorId), creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Account, editorId: Int)  =
    entity.copy(editorId = Some(editorId), editDate = Some(DateUtils.now))

  override def setId(entity: Account, id: Int) = entity.copy(id = id)


  def hasUsers: Future[Boolean] = Future {
    withDb { session => accountRepo.isEmpty(session) }
  }

  def createAccount(user: Account): Future[Account] = {
    // тут мы должны сделать хеш пароля и сохранить его
    val loginInfo = LoginInfo(CredentialsProvider.ID, user.login)
    identityService.retrieve(loginInfo) flatMap {
      case None =>
        // пользователя с таким логином в БД нет
        // хешируем пароль
        val authInfo = passwordHasher.hash(user.passwordHash)
        Future {
          // сохраняем пользователя
          val id = withDb { session => accountRepo.create(user.copy(passwordHash = ""))(session) }
          user.copy(id = id)
        } flatMap { u =>
          // сохраняем хешированный пароль
          authInfoService.save(loginInfo, authInfo) map { _ => u }
        }
      case Some(u) =>
        // пользователь уже существует
        throw new AccountAlreadyExistsException(user.login)
    }
  }

  override def find(userFilter: AccountFilter): List[Tables.Account] = {
    withDb {
      session => accountRepo.find(userFilter)(session)
    }
  }
}


