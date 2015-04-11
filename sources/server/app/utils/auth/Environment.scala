package utils.auth

import com.mohiva.play.silhouette.api.services.{AuthenticatorService, IdentityService}
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables.Account

trait Environment extends com.mohiva.play.silhouette.api.Environment[Account, JWTAuthenticator]

object Environment {
  def apply(
        identityServiceImpl: IdentityService[Account],
        authenticatorServiceImpl: AuthenticatorService[JWTAuthenticator],
        providersImpl: Map[String, Provider],
        eventBusImpl: EventBus) = new Environment {
    val identityService = identityServiceImpl
    val authenticatorService = authenticatorServiceImpl
    val providers = providersImpl
    val eventBus = eventBusImpl
  }
}