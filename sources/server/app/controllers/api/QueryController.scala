package controllers.api

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables.SystemUser
import play.api.i18n.MessagesApi
import service.query.QueryManager
import utils.responses.Response

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class QueryController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[SystemUser, JWTAuthenticator],
  queryManager: QueryManager)
  extends BaseController {

  def run(reportName: String) = SecuredAction.async { implicit request =>
    logger.debug(s"Получен запрос выполнение отчета $reportName с параметрами ${request.queryString}")
    val report = queryManager.getReport(reportName)
    report match {
      case None =>
        logger.debug(s"Отчет $reportName не был найден")
        Future.successful(NotFound(Response.bad(s"Именованный запрос $reportName не найден.")))
      case Some(r) =>
        r.execute(request.queryString) map { jv =>
          logger.debug(s"Отчет $reportName был выполнен")
          Ok(jv)
        } recover {
          case e: Throwable =>
            logger.error(s"Ошибка выполнения запроса $reportName", e)
            InternalServerError(e.toString)
        }
    }
  }
}
