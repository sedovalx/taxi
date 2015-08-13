package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Rent, RentFilter, RentTable, SystemUser}
import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.RentRepo
import serialization.entity.RentSerializer
import service.entity.{CarService, DriverService, RentService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

class RentController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: RentService,
  val serializer: RentSerializer,
  driverService: DriverService,
  carService: CarService)
  extends EntityController[Rent, RentTable, RentRepo, RentFilter, RentSerializer] {

  import serializer._

  override protected val entitiesName: String = "rents"
  override protected val entityName: String = "rent"

  override protected def afterCreate(json: JsValue, entity: Tables.Rent, identity: SystemUser): Tables.Rent = {
    val rent = super.afterCreate(json, entity, identity)
    val status = (json \ "status").as[RentStatus]
    entityService.createNewStatus(rent, status, identity.id)
    rent
  }

  override protected def afterUpdate(json: JsValue, entity: Tables.Rent, identity: Tables.SystemUser): Tables.Rent = {
    val rent = super.afterUpdate(json, entity, identity)
    val status = (json \ "status").as[RentStatus]
    val actual = entityService.getCurrentStatus(rent)
    if (status != actual){
      entityService.createNewStatus(rent, status, identity.id)
    }
    rent
  }

  override protected def afterSerialization(rent: Option[Rent], json: JsValue): Future[JsValue] = {
    /*
    * добавить поле status
    * добавить поле carDisplayName
    * добавить поле driverDisplayName
    * */
    rent match {
      case None => super.afterSerialization(rent, json)
      case Some(r) =>
        val transformFuture = for {
          status <- entityService.getCurrentStatus(r)
          carName <- carService.getDisplayName(r.carId)
          driverName <- driverService.getDisplayName(r.driverId)
        // без внешних скобок это шаманство не работает
        } yield (
          (__ \ "rent").json.pickBranch and
            (__ \ "rent").json.pickBranch(
              (__ \ "status").json.put(JsString(status.toString)) and
                (__ \ "carDisplayName").json.put(JsString(carName)) and
                (__ \ "driverDisplayName").json.put(JsString(driverName))
                reduce
            ) reduce
          )

        transformFuture map { transform =>
          json.transform(transform) match {
            case JsSuccess(obj, _) => obj
            case JsError(errors) =>
              Logger.warn(s"Rent serialization error: $errors")
              json
          }
        }
    }
  }
}
