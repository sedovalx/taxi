package controllers.entities

import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Account, Rent, RentFilter, RentTable}
import play.api.libs.json._
import repository.{DriverRepo, CarRepo, RentRepo}
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

  override protected def afterSerialization(rent: Option[Rent], json: JsValue): JsValue = {
    /*
    * добавить поле status
    * добавить поле carDisplayName
    * добавить поле driverDisplayName
    * */
    rent match {
      case None => super.afterSerialization(rent, json)
      case Some(r) =>
        val status = entityService.getCurrentStatus(r)
        val carName = getCarDisplayName(r)
        val driverName = getDriverDisplayName(r)
        json
    }
  }

  private def getCarDisplayName(rent: Rent): String = {
    val carRepo = inject[CarRepo]
    val Some(car) = carRepo.findById(rent.carId)
    s"${car.model} ${car.regNumber} (${car.rate}})"
  }

  private def getDriverDisplayName(rent: Rent): String = {
    val driverRepo = inject[DriverRepo]
    val Some(driver) = driverRepo.findById(rent.driverId)
    s"${driver.lastName} ${driver.firstName}" +
      (driver.middleName match {
        case Some(s) => " " + s
        case None => ""
      })
  }
}
