package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import scaldi.Module
import utils.auth.{UserServiceImpl, UserService}

import scala.slick.driver.JdbcProfile

class ServicesModule extends Module {

  bind [JdbcProfile] to play.api.db.slick.Config.driver

  bind [UserService] to new UserServiceImpl(
    inject [JdbcProfile],
    inject [PasswordHasher],
    inject [DelegableAuthInfoService],
    inject [IdentityService[Account]])
}
