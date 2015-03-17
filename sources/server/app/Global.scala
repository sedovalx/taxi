import play.api.GlobalSettings
import play.api.mvc._
import java.io.File
import play.api._
import java.net.InetAddress;
import scala.concurrent.Future
import com.typesafe.config.ConfigFactory
import play.filters.gzip.GzipFilter

object RoutesLoggingFilter extends Filter {
  override def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    println(rh.uri)
    next(rh)
  }
}

object Global extends WithFilters(new GzipFilter(), RoutesLoggingFilter) with GlobalSettings {
  override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val host = InetAddress.getLocalHost
    val hostName = host.getHostName
    val machineSpecificConfig = config ++ Configuration(ConfigFactory.load(s"application.$hostName.override.conf"))
    super.onLoadConfig(machineSpecificConfig, path, classloader, mode)
  }
}