package configuration.di

import controllers.{QueryController, IndexController}
import controllers.auth.AuthController
import controllers.entities._
import scaldi.Module

/**
 * Инициализация контроллеров
 */
class PlayModule extends Module {
  binding to new IndexController
  binding to new AccountController
  binding to new DriverController
  binding to new AuthController
  binding to new QueryController
  binding to new CarController
  binding to new RentController
}
