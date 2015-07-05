package serialization.entity

import java.sql.Timestamp

import models.entities.Entity
import play.api.libs.json._
import utils.DateUtils

trait Serialization {
  val dateIso8601Format = DateUtils.iso8601Format
  implicit val timestampReads: Reads[Timestamp] = JsPath.read[String].map { s => new Timestamp(dateIso8601Format.parse(s).getTime) }
  implicit val timestampWrites: Writes[Timestamp] = new Writes[Timestamp] {
    override def writes(o: Timestamp): JsValue = JsString(dateIso8601Format.format(o))
  }
}

abstract class Serializer[E <: Entity[E], F] extends Serialization{
  implicit val filterReads : Reads[F]
  implicit val reads : Reads[E]
  implicit val writes : Writes[E]
}
