package controllers.entities

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{RepairFilter, Repair, RepairTable}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import repository.RepairRepo
import scaldi.Injector
import service.{EntityService, RepairService}

class RepairController(implicit injector: Injector) extends EntityController[Repair, RepairTable, RepairRepo, RepairFilter]()(injector){
  override protected val entityService = inject [RepairService]

  override protected val entitiesName: String = "repairs"
  override protected val entityName: String = "repair"

  override protected implicit val reads: Reads[Tables.Repair] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "repairDate").read[Timestamp] and
      (JsPath \ "cost").read(min[BigDecimal](0)) and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "rent").read[String].map { id => id.toInt } and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(Repair.apply _)
  override protected implicit val writes: Writes[Tables.Repair] = new Writes[Tables.Repair] {
    override def writes(o: Tables.Repair): JsValue = Json.obj(
      "id" -> o.id.toString,
      "rent" -> o.rentId.toString,
      "fineDate" -> dateIso8601Format.format(o.repairDate),
      "cost" -> o.cost.toString(),
      "description" -> o.description,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override protected implicit val filterReads: Reads[Tables.RepairFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "repairDate").readNullable[Timestamp] and
      (JsPath \ "cost").readNullable[BigDecimal] and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "rent").readNullable[String].map { id => id.map(_.toInt) } and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(RepairFilter.apply _)
}
