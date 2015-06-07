package serialization

import java.sql.Timestamp

import models.entities.Entity
import play.api.libs.json.{JsPath, Reads, Writes}
import utils.extensions.DateUtils

trait Serialization {
  val dateIso8601Format = DateUtils.iso8601Format
  implicit val timestampReads: Reads[Timestamp] = JsPath.read[String].map { s => new Timestamp(dateIso8601Format.parse(s).getTime) }
}

abstract class Serializer[E <: Entity[E], F] extends Serialization{
  implicit val filterReads : Reads[F]
  implicit val reads : Reads[E]
  implicit val writes : Writes[E]
}
