package serialization

import java.text.SimpleDateFormat

import play.api.libs.json._

trait EntitySerializer[E] {
  protected implicit val reads: Reads[E]
  protected implicit val writes: Writes[E]
  protected val dateIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

  def toJson(entity: E, root: String): JsValue = {
    makeJson(root, entity)
  }

  def toJson(entities: List[E], root: String): JsValue = {
    makeJson(root, entities)
  }

  def fromJson(json: JsValue): JsResult[E] = json.validate[E]

  private def makeJson[T](root: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(root -> Json.toJson(obj)))
}


