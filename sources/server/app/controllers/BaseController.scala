package controllers

import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc.Controller
import scaldi.Injectable

/**
 * Базовый класс всех контроллеров приложения
 */
abstract class BaseController extends Controller with Injectable {
  protected def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
