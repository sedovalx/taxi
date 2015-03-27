package configuration.di

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.IndexController
import controllers.entities._
import models.entities.User
import scaldi.Module

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {

  implicit val env = inject[Environment[User, JWTAuthenticator]]

  binding to new IndexController
  binding to new UserController
  binding to new DriverController
}
