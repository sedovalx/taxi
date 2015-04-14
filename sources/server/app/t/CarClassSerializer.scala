package t

import java.sql.Date

import models.generated.Tables.CarClass
import play.api.libs.json.Reads._
import play.api.libs.json._

class CarClassSerializer extends EntitySerializer[CarClass] {
  override protected implicit val reads: Reads[CarClass] = (
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "name").read(minLength[String](1)) and
      (JsPath \ "rate").read(min[BigDecimal](0)) and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "comment").readNullable[String]
    )(CarClass.apply _)

  override protected implicit val writes: Writes[CarClass] = new Writes[CarClass] {
    def writes(cc: CarClass) = Json.obj(
      "id" -> cc.id,
      "name" -> cc.name,
      "rate" -> cc.rate,
      "creationDate" -> cc.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> cc.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> cc.creatorId,
      "editor" -> cc.editorId,
      "comment" -> cc.comment
    )
  }
}
