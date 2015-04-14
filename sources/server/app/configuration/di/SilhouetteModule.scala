package configuration.di

import com.mohiva.play.silhouette.api.EventBus
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.{Clock, PasswordHasher}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.impl.util.{BCryptPasswordHasher, SecureRandomIDGenerator}
import models.generated.Tables.Account
import play.api.Play
import play.api.Play.current
import scaldi.Module
import service.{PasswordInfoServiceImpl, LoginInfoServiceImpl, LoginInfoService, PasswordInfoService}
import utils.auth.{Environment}

import scala.slick.driver.JdbcProfile


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
    headerName = Play.configuration.getString("silhouette.authenticator.headerName").getOrElse { "X-Auth-Token" },
    issuerClaim = Play.configuration.getString("silhouette.authenticator.issueClaim").getOrElse { "play-silhouette" },
    encryptSubject = Play.configuration.getBoolean("silhouette.authenticator.encryptSubject").getOrElse { true },
    authenticatorIdleTimeout = Play.configuration.getInt("silhouette.authenticator.authenticatorIdleTimeout"),
    authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").getOrElse { 12 * 60 * 60 },
    sharedSecret = Play.configuration.getString("application.secret").get
  )

  // токен
  val authService = new JWTAuthenticatorService(
    settings = authSettings,
    dao = None,
    idGenerator = new SecureRandomIDGenerator,
    clock = Clock()
  )

  // хранение паролей
  val passwordInfoDAO = new PasswordInfoServiceImpl

  // сервис доступа к хранению паролей
  val authInfoService = new DelegableAuthInfoService(passwordInfoDAO)

  // провайдер... ну вы поняли
  val credentialsProvider = new CredentialsProvider(authInfoService, passwordHasher, Seq(passwordHasher))

  // хранение логинов
  val loginInfoDAO = new LoginInfoServiceImpl

  // экспорт
  binding to Environment(
    loginInfoDAO,
    authService,
    Map(credentialsProvider.id -> credentialsProvider),
    eventBus
  )
  bind [PasswordHasher] to passwordHasher
  bind [DelegableAuthInfoService] to authInfoService
  bind [IdentityService[Account]] to loginInfoDAO
}
