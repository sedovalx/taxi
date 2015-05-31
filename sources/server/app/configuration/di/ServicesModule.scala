package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import repository._
import scaldi.Module
import service._
import service.queries._
import utils.TestModelGenerator

class ServicesModule extends Module {

  val accountServiceBinding = () =>
    new AccountServiceImpl(
      inject [AccountRepo],
      inject [PasswordHasher],
      inject [DelegableAuthInfoService],
      inject [IdentityService[Account]])

  bind [AccountService] to accountServiceBinding()
  // второй раз для тестов, т.к. в них может быть переопределен оригинальный сервис,
  // а учетку админа чем-то создавать все равно нужно
  bind [AccountService] identifiedBy 'accountService2 to accountServiceBinding()
  bind [RentService] to new RentServiceImpl(inject [RentRepo], inject [RentStatusService])
  bind [RentStatusService] to new RentStatusServiceImpl(inject [RentStatusRepo])

  bind [DriverService] to new DriverService { val repo = inject [DriverRepo] }
  bind [CarService] to new CarService { val repo = inject [CarRepo] }
  bind [PaymentService] to new PaymentService { val repo = inject [PaymentRepo] }
  bind [FineService] to new FineService { val repo = inject [FineRepo] }
  bind [RepairService] to new RepairService { val repo = inject [RepairRepo] }

  bind [QueryManager] to new QueryManagerImpl(injectAllOfType [Query])

  bind [BalanceCalculator] to new BalanceCalculatorImpl

  binding to new TestModelGenerator
}
