package configuration.di

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.entities.User
import scaldi.Module
import utils.auth.{UserServiceImpl, UserService}

class ServicesModule extends Module {
  bind [UserService] to new UserServiceImpl(inject [PasswordHasher], inject [DelegableAuthInfoService], inject [IdentityService[User]])
}
