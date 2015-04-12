package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import scaldi.Module
import utils.auth.{AccountServiceImpl, AccountService}

import scala.slick.driver.JdbcProfile

class ServicesModule extends Module {

  bind [AccountService] to new AccountServiceImpl(
    inject [PasswordHasher],
    inject [DelegableAuthInfoService],
    inject [IdentityService[Account]])
}
