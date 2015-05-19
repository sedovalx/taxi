package configuration.di

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.services.{AuthenticatorService, AuthInfoService, IdentityService}
import com.mohiva.play.silhouette.api.util.{Clock, PasswordHasher}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticator, JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.impl.util.{BCryptPasswordHasher, SecureRandomIDGenerator}
import models.generated.Tables.Account
import play.api.Play
import play.api.Play.current
import scaldi.Module
import service.{PasswordInfoServiceImpl, LoginInfoServiceImpl, LoginInfoService, PasswordInfoService}



/**
 * DI контейнер для auth
 * @see DI вообще: http://habrahabr.ru/post/131993/
 *      DI в частности: http://scaldi.org/learn/#overview
 *      Аутентификация: http://silhouette.mohiva.com/v2.0/docs/introduction
 */
class SilhouetteModule extends Module {

  // хешер
  bind[PasswordHasher] to new BCryptPasswordHasher
  bind[EventBus] to new EventBus

  // токен
  binding toProvider new JWTAuthenticatorService (
    // настройки аутентификации
    settings = JWTAuthenticatorSettings(
      headerName = Play.configuration.getString("silhouette.authenticator.headerName").getOrElse { "X-Auth-Token" },
      issuerClaim = Play.configuration.getString("silhouette.authenticator.issueClaim").getOrElse { "play-silhouette" },
      encryptSubject = Play.configuration.getBoolean("silhouette.authenticator.encryptSubject").getOrElse { true },
      authenticatorIdleTimeout = Play.configuration.getInt("silhouette.authenticator.authenticatorIdleTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").getOrElse { 12 * 60 * 60 },
      sharedSecret = Play.configuration.getString("application.secret").get
    ),
    dao = None,
    idGenerator = new SecureRandomIDGenerator,
    clock = Clock()
  )

  bind [LoginInfoService] to injected[LoginInfoServiceImpl]

  // хранение паролей
  bind [PasswordInfoService] to injected[PasswordInfoServiceImpl]


  // провайдер... ну вы поняли
  bind[CredentialsProvider] toProvider new CredentialsProvider(inject[AuthInfoService], inject[PasswordHasher], Seq(inject[PasswordHasher]))


  bind [Map[String, Provider]] toProvider {
    val credentialsProvider = inject [CredentialsProvider]
    Map(credentialsProvider.id -> credentialsProvider)
  }

  bind[DelegableAuthInfoService] toProvider new DelegableAuthInfoService(inject[PasswordInfoService])


  binding toProvider Environment[Account, JWTAuthenticator](
      inject [LoginInfoService],
      inject [AuthenticatorService[JWTAuthenticator]],
      inject [Map[String, Provider]],
      inject [EventBus]
  )

  bind [IdentityService[Account]] to  injected[LoginInfoServiceImpl]
}
