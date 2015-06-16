package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables.SystemUser
import play.api.Logger
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc.{Result, RequestHeader, Controller}
import utils.responses.Response

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Базовый класс всех контроллеров приложения
 */
abstract class BaseController extends Controller with Silhouette[SystemUser, JWTAuthenticator]{
  override val logger = Logger("application." + this.getClass.getName)
  protected def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))


  override protected def onNotAuthenticated(request: RequestHeader): Option[Future[Result]] = {
    Some(Future { Unauthorized(Json.toJson(Response.bad(play.api.i18n.Messages("auth.error.wrong_credentials")))) })
  }

  override protected def onNotAuthorized(request: RequestHeader): Option[Future[Result]] = {
    Some(Future { Forbidden(Json.toJson(Response.bad(play.api.i18n.Messages("auth.error.forbidden")))) })
  }
}
