package utils.serialization

import play.api.data.validation.ValidationError
import play.api.libs.json._

/**
 * Created by ipopkov on 06/04/15.
 * See url: http://semberal.github.io/json-validation-and-error-handling-in-play-2-1.html
 */
object FormatJsError {

  implicit val JsErrorJsonWriter = new Writes[JsError] {
    def writes(o: JsError): JsValue =
      o.errors.foldLeft(Json.obj()) { (res: JsObject, err: (JsPath, scala.Seq[ValidationError])) =>
        val property = err._1.toString().substring(1)
        val value = err._2.map(e => e.message + ":" + e.args.mkString(",")).map(msg => JsString(msg))
        res + (property, JsArray(value))
      }
  }

}
