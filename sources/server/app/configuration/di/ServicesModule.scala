package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import repository._
import service.queries.{Query, QueryManagerImpl, QueryManager}
import scaldi.Module
import service._

class ServicesModule extends Module {

  bind [AccountService] to new AccountServiceImpl(
    inject [AccountRepo],
    inject [PasswordHasher],
    inject [DelegableAuthInfoService],
    inject [IdentityService[Account]])

  bind [DriverService] to new DriverServiceImpl(inject [DriverRepo])
  bind [CarService] to new CarServiceImpl(inject [CarRepo])
  bind [RentService] to new RentServiceImpl(inject [RentRepo], inject [RentStatusRepo])

  bind [QueryManager] to new QueryManagerImpl(injectAllOfType [Query])
}
