import java.io.File
import java.net.InetAddress

import com.typesafe.config.ConfigFactory
import configuration.di._
import play.api.{GlobalSettings, _}
import play.api.mvc._
import play.filters.gzip.GzipFilter
import scaldi.Injector
import scaldi.play.ScaldiSupport

import scala.concurrent.Future

object RoutesLoggingFilter extends Filter {
  override def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    println(rh.uri)
    next(rh)
  }
}

object Global extends WithFilters(new GzipFilter(), RoutesLoggingFilter) with GlobalSettings with ScaldiSupport {
  override def onLoadConfig(config: Configuration, path: File, classLoader: ClassLoader, mode: Mode.Mode): Configuration = {
    val host = InetAddress.getLocalHost
    val hostName = host.getHostName
    val machineSpecificConfig = config ++ Configuration(ConfigFactory.load(s"application.$hostName.override.conf"))
    super.onLoadConfig(machineSpecificConfig, path, classLoader, mode)
  }

  override def applicationModule: Injector = new SilhouetteModule ++ new PlayModule
}