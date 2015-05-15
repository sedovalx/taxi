package service

import javax.security.auth.login.AccountNotFoundException

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.{PasswordInfo, PasswordHasher}
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import controllers.filter.AccountFilter
import models.entities.Role
import models.generated.Tables
import models.generated.Tables.{Account, AccountTable}
import play.api.libs.json.Json
import repository.db.DbAccessor
import repository.AccountRepo
import utils.extensions.DateUtils
import utils.extensions.DateUtils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AccountService extends DbAccessor with EntityService[Account, AccountTable, AccountRepo] {
  def hasUsers: Future[Boolean]
}

class AccountServiceImpl(val repo: AccountRepo,
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

  override protected def find(params: Map[String, String]): List[Account] = {
    val userFilter = tryParseFilterParams(params)
    userFilter match {
      case Some(filter) => withDb { session => repo.find(filter)(session) }
      case None => withDb { session => repo.read(session) }
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

  private def tryParseFilterParams(params: Map[String, String]): Option[AccountFilter] = {
    val login = params.get("login")
    val lastName = params.get("lastName")
    val firstName = params.get("firstName")
    val middleName = params.get("middleName")
    val role = Role.toRole(params.get("role"))
    //TODO: уточнить формат даты
    val createDate = stringToDate ( params.get("createDate") )

    val filter = new AccountFilter(login, lastName, firstName, middleName, role, createDate)

    //TODO: вынести в utils?
    val fieldList = filter.productIterator.toList.collect({ case Some(x) => x })
    val hasAny = fieldList.length > 0

    hasAny match {
      case true => Some(filter)
      case _ => None
    }
  }
}


