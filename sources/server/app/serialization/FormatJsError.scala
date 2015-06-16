package serialization

import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

/**
 * Created by ipopkov on 06/04/15.
 * See url: http://semberal.github.io/json-validation-and-error-handling-in-play-2-1.html
 */
object FormatJsError {

  implicit val JsErrorJsonWriter = new Writes[JsError] {
    def writes(o: JsError): JsValue =
      o.errors.foldLeft(Json.obj()) { (res: JsObject, err: (JsPath, scala.Seq[ValidationError])) =>
        val property = err._1.toString().substring(1)
        val value = formatErrors(err._2).map(msg => JsString(msg))
        res + (property, JsArray(value))
      }
  }

  private def formatError(err: ValidationError): String = {
    play.api.i18n.Messages("validation." + err.message.replaceAll("\\s+", "_"), err.args.toArray:_*)
  }

  private def formatErrors(errors: Seq[ValidationError]): Seq[String] = {
    errors.map(formatError)
  }

}
