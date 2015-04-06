package utils.serialization

import play.api.libs.json._

/**
 * Created by ipopkov on 06/04/15.
 * See url: http://semberal.github.io/json-validation-and-error-handling-in-play-2-1.html
 */
object FormatJsError {

  implicit val JsErrorJsonWriter = new Writes[JsError] {
    def writes(o: JsError): JsValue = JsArray(
        o.errors.map {
          case (path, validationErrors) => Json.obj(
            "path" -> Json.toJson( path.toString().substring(1) ),
            //TODO: подумать о локализации ошибок
            "validationErrors" ->validationErrors.map( validationError => validationError.message )
          )
        }
    )
  }

}
