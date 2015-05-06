package controllers.entities

import java.sql.Timestamp

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Car, CarTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.CarRepo
import scaldi.Injector
import service.CarService

class CarController(implicit injector: Injector) extends EntityController[Car, CarTable, CarRepo] {
  override protected val entityService = inject [CarService]

  override protected def copyEntityWithId(entity: Tables.Car, id: Int): Tables.Car = entity.copy(id = id)

  override protected val entitiesName: String = "cars"
  override protected implicit val reads: Reads[Tables.Car] = (
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "regNumber").read(minLength[String](8)) and
      (JsPath \ "make").read[String] and
      (JsPath \ "carModel").read[String] and
      (JsPath \ "rate").read(min[BigDecimal](0)) and
      (JsPath \ "mileage").read(min[BigDecimal](0)) and
      (JsPath \ "service").readNullable(min[BigDecimal](0)) and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Car.apply _)
  override protected implicit val writes: Writes[Tables.Car] = new Writes[Car] {
    override def writes(o: Tables.Car) = Json.obj(
      "id" -> o.id,
      "regNumber" -> o.regNumber,
      "make" -> o.make,
      "carModel" -> o.model,
      "mileage" -> o.mileage,
      "service" -> o.service.map { s => s.toString() },
      "rate" -> o.rate.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override protected val entityName: String = "car"

  override protected def env = inject [Environment[Tables.Account, JWTAuthenticator]]
}
