package config

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{PasswordInfo, PasswordHasher, Clock}
import com.mohiva.play.silhouette.api.{Environment, EventBus}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticator, JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.repositories.DelegableAuthInfoRepository
import com.mohiva.play.silhouette.impl.util.{BCryptPasswordHasher, SecureRandomIDGenerator}
import models.generated.Tables.SystemUser
import net.codingwell.scalaguice.ScalaModule
import play.api.Play
import service.auth.{PasswordInfoServiceImpl, LoginInfoServiceImpl, LoginInfoService}

import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

import javax.inject.Singleton

class SilhouetteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[LoginInfoService].to[LoginInfoServiceImpl].in[Singleton]
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordInfoServiceImpl].in[Singleton]
  }

  @Provides
  def provideSettings(): JWTAuthenticatorSettings = {
    JWTAuthenticatorSettings(
      headerName = Play.configuration.getString("silhouette.authenticator.headerName").getOrElse { "X-Auth-Token" },
      issuerClaim = Play.configuration.getString("silhouette.authenticator.issueClaim").getOrElse { "play-silhouette" },
      encryptSubject = Play.configuration.getBoolean("silhouette.authenticator.encryptSubject").getOrElse { true },
      authenticatorIdleTimeout = Play.configuration.getInt("silhouette.authenticator.authenticatorIdleTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").getOrElse { 12 * 60 * 60 },
      sharedSecret = Play.configuration.getString("application.secret").get
    )
  }

  @Provides
  def provideAuthenticatorService(settings: JWTAuthenticatorSettings): JWTAuthenticatorService = {
    new JWTAuthenticatorService(settings, None, new SecureRandomIDGenerator, Clock())
  }

  @Provides
  def provideEnvironment(
    userService: LoginInfoService,
    authenticatorService: JWTAuthenticatorService,
    eventBus: EventBus): Environment[SystemUser, JWTAuthenticator] = {

    Environment(userService, authenticatorService, Seq(), eventBus)
  }

  @Provides
  def provideCredentialsProvider(authInfoRepository: AuthInfoRepository, passwordHasher: PasswordHasher): CredentialsProvider = {
    new CredentialsProvider(authInfoRepository, passwordHasher, Seq(passwordHasher))
  }

  @Provides
  def provideAuthInfoRepository(passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoDAO)
  }
}
