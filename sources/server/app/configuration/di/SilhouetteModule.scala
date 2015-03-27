package configuration.di

import com.mohiva.play.silhouette.api.EventBus
import com.mohiva.play.silhouette.api.util.{Clock, PasswordHasher}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.impl.util.{BCryptPasswordHasher, SecureRandomIDGenerator}
import scaldi.Module
import utils.auth.{Environment, LoginInfoDAO, PasswordInfoDAO}



/**
 * DI контейнер для auth
 * @see DI вообще: http://habrahabr.ru/post/131993/
 *      DI в частности: http://scaldi.org/learn/#overview
 *      Аутентификация: http://silhouette.mohiva.com/v2.0/docs/introduction
 */
class SilhouetteModule extends Module {
  // хешер
  val passwordHasher = new BCryptPasswordHasher
  val eventBus = EventBus()

  // настройки аутентификации
  val authSettings = JWTAuthenticatorSettings(
    headerName = inject [String] (identified by "silhouette.authenticator.headerName" and by default "X-Auth-Token"),
    issuerClaim = inject [String] (identified by "silhouette.authenticator.issueClaim" and by default "play-silhouette"),
    encryptSubject = inject [Boolean] (identified by "silhouette.authenticator.encryptSubject" and by default true),
    authenticatorIdleTimeout = Some(inject [Int] (identified by "silhouette.authenticator.authenticatorIdleTimeout" and by default 1800)),
    authenticatorExpiry = inject [Int] (identified by "silhouette.authenticator.authenticatorExpiry" and by default 12 * 60 * 60),
    sharedSecret = inject [String] (identified by "application.secret")
  )

  // токен
  val authService = new JWTAuthenticatorService(
    settings = inject [JWTAuthenticatorSettings],
    dao = None,
    idGenerator = new SecureRandomIDGenerator,
    clock = Clock()
  )

  // хранение паролей
  val passwordInfoDAO = new PasswordInfoDAO

  // сервис доступа к хранению паролей
  val authInfoService = new DelegableAuthInfoService(passwordInfoDAO)

  // провайдер... ну вы поняли
  val credentialsProvider = new CredentialsProvider(authInfoService, passwordHasher, Seq(passwordHasher))

  // хранение логинов
  val loginInfoDAO = new LoginInfoDAO

  // экспорт
  binding to Environment(
    loginInfoDAO,
    authService,
    Map(credentialsProvider.id -> credentialsProvider),
    eventBus
  )
  bind [PasswordHasher] to passwordHasher
  bind [DelegableAuthInfoService] to authInfoService
}
