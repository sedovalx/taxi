package serialization.entity

import java.sql.Timestamp

import models.entities.AccountType
import models.entities.AccountType.AccountType
import models.generated.Tables
import models.generated.Tables.{Operation, OperationFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Json, Reads, Writes}
import serialization.EnumSerializer

class OperationSerializer extends Serializer[Operation, OperationFilter] {
  private implicit val enumReads = EnumSerializer.enumReads(AccountType)
  private implicit val enumWrites = EnumSerializer.enumWrites

  override implicit val reads: Reads[Tables.Operation] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "rent").read[String].map { id => id.toInt } and
      (JsPath \ "amount").read(min[BigDecimal](0)) and
      (JsPath \ "changeTime").read[Timestamp] and
      (JsPath \ "accountType").read[AccountType] and
      (JsPath \ "presence").readNullable[Boolean].map { case Some(s) => s case None => true } and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Operation.apply _)
  override implicit val writes: Writes[Tables.Operation] = new Writes[Operation] {
    override def writes(o: Tables.Operation) = Json.obj(
      "id" -> o.id.toString,
      "rent" -> o.rentId.toString,
      "presence" -> o.presence,
      "changeTime" -> dateIso8601Format.format(o.changeTime),
      "amount" -> o.amount.toString,
      "accountType" -> o.accountType.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override implicit val filterReads: Reads[Tables.OperationFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "rent").readNullable[String].map { id => id.map(_.toInt) } and
      (JsPath \ "amount").readNullable[BigDecimal] and
      (JsPath \ "changeTime").readNullable[Timestamp] and
      (JsPath \ "accountType").readNullable[AccountType] and
      (JsPath \ "presence").readNullable[Boolean] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(OperationFilter.apply _)
}
