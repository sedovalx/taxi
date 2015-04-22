package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import controllers.{QueryController, IndexController}
import controllers.auth.AuthController
import controllers.entities._
import models.generated.Tables.Account
import queries.QueryManager
import scaldi.Module
import utils.auth.{Environment, AccountService}

import scala.slick.driver.JdbcProfile

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {
  binding to new IndexController
  binding to new AccountController(inject [Environment], inject [AccountService])
  binding to new DriverController(inject [Environment], inject [AccountService])
  binding to new AuthController(inject [Environment], inject [IdentityService[Account]])
  binding to new QueryController(inject [Environment], inject [QueryManager])
}
