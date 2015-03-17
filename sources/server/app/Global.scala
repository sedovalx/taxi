import play.api.GlobalSettings
import play.api.mvc.{EssentialAction, Filter, RequestHeader, Result}
import java.io.File
import play.api._
import java.net.InetAddress;
import scala.concurrent.Future
import com.typesafe.config.ConfigFactory

object RoutesLoggingFilter extends Filter {
  override def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    println(rh.uri)
    next(rh)
  }
}

object Global extends GlobalSettings {
  override def doFilter(action: EssentialAction) = RoutesLoggingFilter(action)

  override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val host = InetAddress.getLocalHost
    val hostName = host.getHostName
    val machineSpecificConfig = config ++ Configuration(ConfigFactory.load(s"application.$hostName.override.conf"))
    super.onLoadConfig(machineSpecificConfig, path, classloader, mode)
  }
}