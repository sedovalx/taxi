package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import controllers.IndexController
import controllers.auth.AuthController
import controllers.entities._
import models.entities.User
import scaldi.Module
import utils.auth.{Environment, UserService}

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {
  binding to new IndexController
  binding to new UserController(inject [Environment], inject [UserService])
  binding to new DriverController(inject [Environment], inject [UserService])
  binding to new AuthController(inject [Environment], inject [IdentityService[User]])
}
