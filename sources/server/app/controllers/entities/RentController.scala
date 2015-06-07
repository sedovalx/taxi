package controllers.entities

import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Account, Rent, RentFilter, RentTable}
import play.api.libs.json._
import repository.RentRepo
import scaldi.Injector
import serialization.RentSerializer
import service.RentService

class RentController(implicit injector: Injector) extends EntityController[Rent, RentTable, RentRepo, RentFilter, RentSerializer]()(injector) {
  override protected val entityService = inject [RentService]
  override protected val serializer: RentSerializer = inject[RentSerializer]
  import serializer._

  override protected val entitiesName: String = "rents"
  override protected val entityName: String = "rent"

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
