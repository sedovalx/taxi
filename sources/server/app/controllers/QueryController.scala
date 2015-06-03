package controllers

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables.Account
import play.api.Logger
import play.api.mvc.Action
import service.queries.QueryManager
import scaldi.Injector

class QueryController(implicit inj: Injector) extends BaseController with Silhouette[Account, JWTAuthenticator]{

  implicit val env = inject [Environment[Account, JWTAuthenticator]]
  val queryManager = inject [QueryManager]

  def run(reportName: String) = Action { implicit request =>
    val report = queryManager.getReport(reportName)
    report match {
      case None => NotFound(Response.bad(s"Именованный запрос $reportName не найден."))
      case Some(r) =>
        try{
          Ok(r.execute(request.queryString))
        }
        catch {
          case e: Throwable =>
            Logger.error(s"Ошибка выполнения запроса $reportName", e)
            InternalServerError(e.toString)
        }

    }
  }
}
