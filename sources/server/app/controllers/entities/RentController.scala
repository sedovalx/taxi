package controllers.entities

import java.sql.Timestamp

import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Account, Rent, RentTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.RentRepo
import scaldi.Injector
import service.RentService
import utils.serialization.EnumSerializer

class RentController(implicit injector: Injector) extends EntityController[Rent, RentTable, RentRepo]()(injector) {
  override protected val entityService = inject [RentService]

  override protected val entitiesName: String = "rents"
  override protected val entityName: String = "rent"

  override protected implicit val reads: Reads[Tables.Rent] = (
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "driver").read[String].map { id => id.toInt } and
      (JsPath \ "car").read[String].map { id => id.toInt } and
      (JsPath \ "deposit").read[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp]
    )(Rent.apply _)
  override protected implicit val writes: Writes[Tables.Rent] = new Writes[Rent] {
    override def writes(o: Tables.Rent) = Json.obj(
      "id" -> o.id.toString,
      "driver" -> o.driverId.toString,
      "car" -> o.carId.toString,
      "deposit" -> o.deposit.toString(),
      "status" -> entityService.getCurrentStatus(o).toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }

  private implicit val enumReads = EnumSerializer.enumReads(RentStatus)

  override protected def afterCreate(json: JsValue, entity: Tables.Rent, identity: Account): Tables.Rent = {
    val rent = super.afterCreate(json, entity, identity)
    val status = (json \ "status").as[RentStatus]
    entityService.createNewStatus(rent, status, Some(identity.id))
    rent
  }

  override protected def afterUpdate(json: JsValue, entity: Tables.Rent, identity: Tables.Account): Tables.Rent = {
    val rent = super.afterUpdate(json, entity, identity)
    val status = (json \ "status").as[RentStatus]
    val actual = entityService.getCurrentStatus(rent)
    if (status != actual){
      entityService.createNewStatus(rent, status, Some(identity.id))
    }
    rent
  }
}
