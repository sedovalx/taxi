package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import controllers.IndexController
import controllers.auth.AuthController
import controllers.entities._
import models.generated.Tables.Account
import repository.DriversRepo
import scaldi.Module
import service.{DriverService, AccountService}
import utils.auth.Environment

import scala.slick.driver.JdbcProfile

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {
  binding to new IndexController
  binding to new AccountController(inject [Environment], inject [AccountService])
  binding to new DriverController(inject [Environment], inject [AccountService], inject[DriverService])
  binding to new AuthController(inject [Environment], inject [IdentityService[Account]])
}
