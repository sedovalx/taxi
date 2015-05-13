package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import repository._
import scaldi.Module
import service._
import service.queries.{Query, QueryManager, QueryManagerImpl}

class ServicesModule extends Module {

  bind [AccountService] to new AccountServiceImpl(
    inject [AccountRepo],
    inject [PasswordHasher],
    inject [DelegableAuthInfoService],
    inject [IdentityService[Account]])
  bind [RentService] to new RentServiceImpl(inject [RentRepo], inject [RentStatusService])
  bind [RentStatusService] to new RentStatusServiceImpl(inject [RentStatusRepo])

  bind [DriverService] to new DriverService { val repo = inject [DriverRepo] }
  bind [CarService] to new CarService { val repo = inject [CarRepo] }
  bind [PaymentService] to new PaymentService { val repo = inject [PaymentRepo] }
  bind [FineService] to new FineService { val repo = inject [FineRepo] }
  bind [RepairService] to new RepairService { val repo = inject [RepairRepo] }

  bind [QueryManager] to new QueryManagerImpl(injectAllOfType [Query])
}
