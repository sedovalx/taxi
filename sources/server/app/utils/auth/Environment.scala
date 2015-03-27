package utils.auth

import com.mohiva.play.silhouette.api.services.{AuthenticatorService, IdentityService}
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.entities.User

trait Environment extends com.mohiva.play.silhouette.api.Environment[User, JWTAuthenticator]

object Environment {
  def apply(
        identityServiceImpl: IdentityService[User],
        authenticatorServiceImpl: AuthenticatorService[JWTAuthenticator],
        providersImpl: Map[String, Provider],
        eventBusImpl: EventBus) = new Environment {
    val identityService = identityServiceImpl
    val authenticatorService = authenticatorServiceImpl
    val providers = providersImpl
    val eventBus = eventBusImpl
  }
}