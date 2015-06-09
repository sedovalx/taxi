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
  binding to new SystemUserController
  binding to new DriverController
  binding to new AuthController
  binding to new QueryController
  binding to new CarController
  binding to new RentController
  binding to new PaymentController
  binding to new FineController
  binding to new RepairController
  binding to injected [TestController]
}
