package utils.responses

import play.api.libs.json.{JsValue, Json}

case class Response(status: String, message: String, desc: String)

object Response {
  private implicit val responseFormat = Json.format[Response]

  def good(message: String) = Json.toJson(Response("ok", message, ""))
  def bad(message: String, error: String): JsValue = Json.toJson(Response("error", message, error))
  def bad(message: String, error: JsValue): JsValue = Response.bad(message, Json.stringify(error))
  def bad(message: String): JsValue = Response.bad(message, "")
}
