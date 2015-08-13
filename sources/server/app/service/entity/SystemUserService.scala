package service.entity

import javax.inject.Inject
import javax.security.auth.login.AccountNotFoundException

import com.mohiva.play.silhouette.api.util.{PasswordHasher, PasswordInfo}
import models.generated.Tables
import models.generated.Tables.{SystemUser, SystemUserFilter, SystemUserTable}
import play.api.libs.json.Json
import repository.SystemUserRepo
import service.validation.{CommonValidationError, ValidationResult, SuccessfulResult, ValidationError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait SystemUserService extends EntityService[SystemUser, SystemUserTable, SystemUserRepo, SystemUserFilter] {
  def hasUsers: Future[Boolean]
}

class SystemUserServiceImpl @Inject() (
  val repo: SystemUserRepo,
  passwordHasher: PasswordHasher)
  extends SystemUserService {

  implicit val passwordFormat = Json.format[PasswordInfo]

  def hasUsers: Future[Boolean] = repo.isEmpty

  override protected def validateBeforeDelete(entity: SystemUser, performerId: Int): Future[ValidationResult] = {
    super.validateBeforeDelete(entity, performerId) map {
      case r: SuccessfulResult =>
        checkIfSelfDelete(entity, performerId) match {
          case Some(e) => ValidationResult.notAcceptable(e.message)
          case None => ValidationResult.success()
        }
      case r => r
    }
  }

  override protected def beforeCreate(entity: Tables.SystemUser, creatorId: Option[Int]): Future[Tables.SystemUser] = {
    super.beforeCreate(entity, creatorId) map { toSave =>
      // перед сохранением нужно посчитать хеш пароля
      val authInfo = passwordHasher.hash(toSave.passwordHash)
      val passwordHash = Json.toJson(authInfo).toString()
      toSave.copy(passwordHash = passwordHash)
    }
  }

  override protected def beforeUpdate(entity: Tables.SystemUser, editorId: Int): Future[Tables.SystemUser] = {
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

  private def checkIfSelfDelete(entity: SystemUser, performerId: Int): Option[ValidationError] = {
    if (entity.id == performerId)
      Some(new CommonValidationError("Удалять самого себя запрещено"))
    else None
  }
}


