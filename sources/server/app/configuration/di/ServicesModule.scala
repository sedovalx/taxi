package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.SystemUser
import repository._
import scaldi.Module
import service._
import service.queries._
import utils.TestModelGenerator

class ServicesModule extends Module {

  val accountServiceBinding = () =>
    new SystemUserServiceImpl(
      inject [SystemUserRepo],
      inject [PasswordHasher],
      inject [DelegableAuthInfoService],
      inject [IdentityService[SystemUser]])

  bind [SystemUserService] to accountServiceBinding()
  // второй раз для тестов, т.к. в них может быть переопределен оригинальный сервис,
  // а учетку админа чем-то создавать все равно нужно
  bind [SystemUserService] identifiedBy 'accountService2 to accountServiceBinding()
  bind [RentService] to new RentServiceImpl(inject [RentRepo], inject [RentStatusService])
  bind [RentStatusService] to new RentStatusServiceImpl(inject [RentStatusRepo])

  bind [DriverService] to new DriverServiceImpl(inject [DriverRepo])
  bind [CarService] to new CarServiceImpl(inject[CarRepo])
  bind [OperationService] to new OperationService { val repo = inject [OperationRepo] }

  bind [QueryManager] to new QueryManagerImpl(injectAllOfType [Query])

  binding to new TestModelGenerator
}
