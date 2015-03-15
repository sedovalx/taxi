import play.api.GlobalSettings
import play.api.mvc.{EssentialAction, Filter, RequestHeader, Result}

import scala.concurrent.Future

object RoutesLoggingFilter extends Filter {
  override def apply(next: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    println(rh.uri)
    next(rh)
  }
}

object Global extends GlobalSettings {
  override def doFilter(action: EssentialAction) = RoutesLoggingFilter(action)
}