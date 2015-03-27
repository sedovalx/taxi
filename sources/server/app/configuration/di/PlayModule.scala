package configuration.di

import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import controllers.IndexController
import controllers.auth.AuthController
import controllers.entities._
import scaldi.Module
import utils.auth.Environment

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {
  binding to new IndexController
  binding to new UserController(inject [Environment], inject [PasswordHasher], inject [DelegableAuthInfoService])
  binding to new DriverController
  binding to new AuthController(inject [Environment])
}
