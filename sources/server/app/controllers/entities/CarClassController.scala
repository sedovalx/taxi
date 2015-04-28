package controllers.entities

import java.sql.Date

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{CarClass, CarClassTable}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import repository.CarClassRepo
import scaldi.Injector
import service.CarClassService

class CarClassController(implicit injector: Injector) extends EntityController[CarClass, CarClassTable, CarClassRepo] {
  override protected val entityService = inject[CarClassService]
  override protected def env = inject [Environment[Tables.Account, JWTAuthenticator]]

  override protected val entitiesName: String = "carClasses"
  override protected val entityName: String = "carClass"

  override protected def copyEntityWithId(entity: CarClass, id: Int) = entity.copy(id = id)

  override protected implicit val reads: Reads[Tables.CarClass] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "name").read[String] and
      (JsPath \ "rate").read[BigDecimal] and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "comment").readNullable[String]
    )(CarClass.apply _)
  override protected implicit val writes: Writes[Tables.CarClass] = new Writes[CarClass] {
    def writes(cc: CarClass) = Json.obj(
      "id" -> cc.id,
      "name" -> cc.name,
      "rate" -> cc.rate,
      "creationDate" -> cc.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> cc.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> cc.creatorId,
      "editor" -> cc.editorId,
      "comment" -> cc.comment
    )
  }



}
