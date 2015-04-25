package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import repository.{DriversRepo, CarClassRepo, AccountRepo}
import service.queries.{Query, QueryManagerImpl, QueryManager}
import scaldi.Module
import service._


//import scala.slick.driver.JdbcProfile

class ServicesModule extends Module {

  bind [AccountService] to new AccountServiceImpl(
    inject [AccountRepo],
    inject [PasswordHasher],
    inject [DelegableAuthInfoService],
    inject [IdentityService[Account]])

  bind [CarClassService] to new CarClassServiceImpl(
    inject [CarClassRepo]
  )

  bind [DriverService] to new DriverServiceImpl(
    inject [DriversRepo]

  )

  bind [QueryManager] to new QueryManagerImpl(injectAllOfType [Query])
}
