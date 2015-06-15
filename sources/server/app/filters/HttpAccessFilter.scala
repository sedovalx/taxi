package filters

import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HttpAccessFilter extends Filter {

  val accessLogger = Logger(this.getClass.getName)

  def apply(next: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    val resultFuture = next(request)

    resultFuture.foreach(result => {
      val msg = s"${request.method} ${request.uri} from ${request.remoteAddress}" +
        s" with result ${result.header.status}"
      accessLogger.info(msg)
    })

    resultFuture
  }
}
