package controllers.entities

import java.sql.Date

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{CarClass, CarClassTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
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
      (JsPath \ "rate").read(min[BigDecimal](0)) and
      (JsPath \ "creationDate").readNullable[Date] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Date] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "comment").readNullable[String]
    )(CarClass.apply _)
  override protected implicit val writes: Writes[Tables.CarClass] = new Writes[CarClass] {
    def writes(o: CarClass) = Json.obj(
      "id" -> o.id.toString,
      "name" -> o.name,
      "rate" -> o.rate.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
}
