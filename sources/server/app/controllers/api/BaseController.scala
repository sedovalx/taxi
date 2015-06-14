package controllers.api

import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc.Controller

/**
 * Базовый класс всех контроллеров приложения
 */
abstract class BaseController extends Controller {
  protected def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
