package serialization

import java.sql.Timestamp

import models.entities.Entity
import play.api.libs.json.{JsPath, Reads, Writes}
import utils.extensions.DateUtils

abstract class Serializer[E <: Entity[E], F] {
  protected val dateIso8601Format = DateUtils.iso8601Format
  protected implicit val timestampReads: Reads[Timestamp] = JsPath.read[String].map { s => new Timestamp(dateIso8601Format.parse(s).getTime) }

  implicit val filterReads : Reads[F]
  implicit val reads : Reads[E]
  implicit val writes : Writes[E]
}
