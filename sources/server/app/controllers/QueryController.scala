package controllers

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables.Account
import queries.QueryManager
import utils.auth.Environment

class QueryController(val env: Environment,
                        queryManager: QueryManager)
  extends BaseController with Silhouette[Account, JWTAuthenticator]{

  def run(reportName: String) = SecuredAction { implicit request =>
    val report = queryManager.getReport(reportName)
    report match {
      case None => NotFound(Response.bad(s"Именованный запрос $reportName не найден."))
      case Some(r) =>
        Ok(r.execute(request.queryString))
    }
  }
}
