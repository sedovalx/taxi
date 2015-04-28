package controllers.entities

import java.sql.Date

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import repository.DriversRepo
import scaldi.Injector
import service.DriverService

/**
 * Контроллер операций над водителями
 */
class DriverController(implicit injector: Injector) extends EntityController[Driver, DriverTable, DriversRepo] {
  override val entityService = inject [DriverService]

  override protected def copyEntityWithId(entity: Driver, id: Int): Driver = entity.copy(id = id)

  override val entitiesName: String = "drivers"
  override implicit val reads: Reads[Tables.Driver] = (
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "pass").read[String] and
      (JsPath \ "license").read[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "phone").read[String] and
      (JsPath \ "secPhone").read[String] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "address").read[String] and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Driver.apply _)
  override implicit val writes: Writes[Tables.Driver] = new Writes[Driver] {
    def writes(driver: Driver) = Json.obj(
      "id" -> driver.id,
      "pass" -> driver.pass,
      "license" -> driver.license,
      "lastName" -> driver.lastName,
      "firstName" -> driver.firstName,
      "middleName" -> driver.middleName,
      "phone" -> driver.phone,
      "secPhone" -> driver.secPhone,
      "address" -> driver.address,
      "creationDate" -> driver.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> driver.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> driver.creatorId,
      "editor" -> driver.editorId,
      "comment" -> driver.comment
    )
  }
  override val entityName: String = "driver"

  override protected def env = inject [Environment[Tables.Account, JWTAuthenticator]]
}