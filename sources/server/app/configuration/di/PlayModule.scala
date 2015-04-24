package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import controllers.IndexController
import controllers.auth.AuthController
import controllers.entities._
import models.generated.Tables.Account
import scaldi.Module
import service.{DriverService, AccountService}

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {
  binding to new IndexController
  binding to new AccountController
  binding to new DriverController
  binding to new AuthController
}
