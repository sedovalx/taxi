package controllers.entities

import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{SystemUser, Rent, RentFilter, RentTable}
import play.api.Logger
import play.api.libs.json._
import repository.RentRepo
import scaldi.Injector
import serialization.RentSerializer
import service.{CarService, DriverService, RentService}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

import scala.language.postfixOps

class RentController(implicit injector: Injector) extends EntityController[Rent, RentTable, RentRepo, RentFilter, RentSerializer]()(injector) {
  override protected val entityService = inject [RentService]
  override protected val serializer: RentSerializer = inject[RentSerializer]
  import serializer._

  override protected val entitiesName: String = "rents"
  override protected val entityName: String = "rent"

  private val driverService = inject[DriverService]
  private val carService = inject[CarService]

  override protected def afterCreate(json: JsValue, entity: Tables.Rent, identity: SystemUser): Tables.Rent = {
    val rent = super.afterCreate(json, entity, identity)
    val status = (json \ "status").as[RentStatus]
    entityService.createNewStatus(rent, status, Some(identity.id))
    rent
  }

  override protected def afterUpdate(json: JsValue, entity: Tables.Rent, identity: Tables.SystemUser): Tables.Rent = {
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
        val carName = carService.getDisplayName(r.carId)
        val driverName = driverService.getDisplayName(r.driverId)

        // без внешних скобок это шаманство не работает
        val transform = (
            (__ \ "rent").json.pickBranch and
            (__ \ "rent").json.pickBranch(
              (__ \ "status").json.put(JsString(status.toString)) and
              (__ \ "carDisplayName").json.put(JsString(carName)) and
              (__ \ "driverDisplayName").json.put(JsString(driverName))
                reduce
            ) reduce
          )

        json.transform(transform) match {
          case JsSuccess(obj, _) => obj
          case JsError(errors) =>
            Logger.warn(s"Rent serialization error: $errors")
            json
        }

    }
  }
}
