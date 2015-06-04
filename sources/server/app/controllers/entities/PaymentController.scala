package controllers.entities

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{PaymentFilter, Payment, PaymentTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.PaymentRepo
import scaldi.Injector
import service.PaymentService

class PaymentController(implicit injector: Injector) extends EntityController[Payment, PaymentTable, PaymentRepo, PaymentFilter]()(injector) {
  override protected val entityService = inject [PaymentService]

  override protected val entitiesName: String = "payments"
  override protected val entityName: String = "payment"

  override protected implicit val reads: Reads[Tables.Payment] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "changeTime").read[Timestamp] and
      (JsPath \ "presence").readNullable[String].map { case Some(s) => s.toBoolean case None => false } and
      (JsPath \ "amount").read(min[BigDecimal](0)) and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "rent").read[String].map { id => id.toInt }
    )(Payment.apply _)
  override protected implicit val writes: Writes[Tables.Payment] = new Writes[Payment] {
    override def writes(o: Tables.Payment) = Json.obj(
      "id" -> o.id.toString,
      "rent" -> o.rentId.toString,
      "presence" -> o.presence.toString,
      "changeTime" -> dateIso8601Format.format(o.changeTime),
      "amount" -> o.amount.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override protected implicit val filterReads: Reads[Tables.PaymentFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "changeTime").readNullable[Timestamp] and
      (JsPath \ "presence").readNullable[String].map { s => s.map(_.toBoolean) } and
      (JsPath \ "amount").readNullable[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "rent").readNullable[String].map { id => id.map(_.toInt) }
    )(PaymentFilter.apply _)
}
