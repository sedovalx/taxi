package utils

import javax.inject.Inject

import models.entities.Role
import models.generated.Tables.SystemUser
import play.api.Logger
import service.entity.SystemUserService

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class AppBootstrapper @Inject() (userService: SystemUserService) {
  val f = userService.hasUsers flatMap { hasUsers =>
    if (!hasUsers) {
      val admin = SystemUser(id = 0, login = "admin", passwordHash = "admin", role = Role.Administrator)
      userService.create(admin, None)
    } else Future.successful(None)
  } recoverWith {
    case error => Future {
      Logger.error("Ошибка создания учетной записи администратора при старте приложения.", error)
    }
  }
  Await.ready(f, 10 seconds)
}
