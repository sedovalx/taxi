package controllers.entities

import java.sql.Timestamp

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Rent, RentTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.RentRepo
import scaldi.Injector
import service.RentService

class RentController(implicit injector: Injector) extends EntityController[Rent, RentTable, RentRepo] {
  override protected val entityService = inject [RentService]

  override protected def copyEntityWithId(entity: Tables.Rent, id: Int): Tables.Rent = entity.copy(id = id)

  override protected val entitiesName: String = "rents"

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
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override protected val entityName: String = "rent"

  override protected def env = inject [Environment[Tables.Account, JWTAuthenticator]]


}
