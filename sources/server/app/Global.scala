import java.io.File
import java.net.InetAddress

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.SecuredSettings
import com.typesafe.config.ConfigFactory
import configuration.di._
import models.entities.Role
import models.generated.Tables.Account
import play.api.i18n.Lang
import play.api.libs.json.Json
import play.api.{GlobalSettings, _}
import play.api.mvc._
import play.filters.gzip.GzipFilter
import scaldi.{Injectable, Injector}
import scaldi.play.ScaldiSupport
import play.api.mvc.Results._
import service.AccountService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

object RoutesLoggingFilter extends Filter {
  override def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    println(rh.uri)
    next(rh)
  }
}

object Global extends WithFilters(new GzipFilter(), RoutesLoggingFilter) with GlobalSettings with ScaldiSupport with SecuredSettings with Injectable {
  override def onLoadConfig(config: Configuration, path: File, classLoader: ClassLoader, mode: Mode.Mode): Configuration = {
    val host = InetAddress.getLocalHost
    val hostName = host.getHostName
    val machineSpecificConfig = config ++ Configuration(ConfigFactory.load(s"application.$hostName.override.conf"))
    super.onLoadConfig(machineSpecificConfig, path, classLoader, mode)
  }

  override def onNotAuthenticated(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    Some(Future { Unauthorized(Json.toJson(Response.bad("Пользователь с такой комбинацией логина и пароля не найден."))) })
  }

  override def onNotAuthorized(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    Some(Future { Forbidden(Json.toJson(Response.bad("Действие запрещено."))) })
  }

  override def applicationModule: Injector = new SilhouetteModule ++ new RepoModule ++ new ServicesModule ++ new PlayModule


  override def beforeStart(app: Application): Unit = super.beforeStart(app)

  override def onStart(app: Application) = {
    super.onStart(app)

    // создание пользователя, если не найдено
    implicit val injector = applicationModule
    val userService = inject [AccountService]
    val f = userService.hasUsers flatMap { hasUsers =>
      if (!hasUsers) {
        val admin = Account(id = 0, login = "admin", passwordHash = "admin", role = Role.Administrator)
        userService.createAccount(admin)
      } else Future.successful(None)
    } recoverWith {
      case error => Future {
        Logger.error("Ошибка создания учетной записи администратора при старте приложения.", error)
      }
    }
    Await.ready(f, 10 seconds)
  }


}