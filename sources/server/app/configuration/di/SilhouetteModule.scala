package configuration.di

import com.mohiva.play.silhouette.api.{EventBus, Environment, Provider}
import com.mohiva.play.silhouette.api.services.{AuthenticatorService, IdentityService}
import com.mohiva.play.silhouette.api.util.{PasswordInfo, Clock, IDGenerator, PasswordHasher}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticatorService, JWTAuthenticator, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.daos.{DelegableAuthInfoDAO, AuthenticatorDAO}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.impl.util.{SecureRandomIDGenerator, BCryptPasswordHasher}
import models.entities.User
import scaldi.Module
import utils.auth.{PasswordInfoDAO, LoginInfoDAO}



/**
 * DI контейнер для auth
 * @see http://scaldi.org/learn/#overview
 *      http://silhouette.mohiva.com/v2.0/docs/introduction
 */
class SilhouetteModule extends Module {
  // хешер
  bind [PasswordHasher] to new BCryptPasswordHasher

  binding to EventBus()

  // настройки аутентификации
  binding to JWTAuthenticatorSettings(
    headerName = inject [String] (identified by "silhouette.authenticator.headerName" and by default "X-Auth-Token"),
    issuerClaim = inject [String] (identified by "silhouette.authenticator.issueClaim" and by default "play-silhouette"),
    encryptSubject = inject [Boolean] (identified by "silhouette.authenticator.encryptSubject" and by default true),
    authenticatorIdleTimeout = Some(inject [Int] (identified by "silhouette.authenticator.authenticatorIdleTimeout" and by default 1800)),
    authenticatorExpiry = inject [Int] (identified by "silhouette.authenticator.authenticatorExpiry" and by default 12 * 60 * 60),
    sharedSecret = inject [String] (identified by "application.secret")
  )

  // токен
  bind [AuthenticatorService[JWTAuthenticator]] to new JWTAuthenticatorService(
    settings = inject [JWTAuthenticatorSettings],
    dao = None,
    idGenerator = new SecureRandomIDGenerator,
    clock = Clock()
  )

  // хранение паролей
  bind [DelegableAuthInfoDAO[PasswordInfo]] to new PasswordInfoDAO

  // сервис доступа к хранению паролей
  binding to new DelegableAuthInfoService(injectAllOfType [DelegableAuthInfoDAO[PasswordInfo]] :_*)

  // провайдер... ну вы поняли
  binding to new CredentialsProvider(inject [DelegableAuthInfoService], inject [PasswordHasher], Seq(inject [PasswordHasher]))

  // хранение логинов
  bind [IdentityService[User]] to new LoginInfoDAO

  binding to createEnvironment(
    inject [IdentityService[User]],
    inject [AuthenticatorService[JWTAuthenticator]],
    inject [CredentialsProvider],
    inject [EventBus]
  )

  def createEnvironment(identityService: IdentityService[User],
                        authService: AuthenticatorService[JWTAuthenticator],
                        credentialsProvider: CredentialsProvider,
                        eventBus: EventBus) = {
    Environment[User, JWTAuthenticator](
      identityService,
      authService,
      Map(credentialsProvider.id -> credentialsProvider),
      eventBus
    )
  }


}
