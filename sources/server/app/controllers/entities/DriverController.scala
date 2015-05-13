package controllers.entities

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables._
import org.postgresql.util.PSQLException
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.Result
import repository.DriverRepo
import scaldi.Injector
import service.DriverService

/**
 * Контроллер операций над водителями
 */
class DriverController(implicit injector: Injector) extends EntityController[Driver, DriverTable, DriverRepo]()(injector) {
  override val entityService = inject [DriverService]

  override protected def copyEntityWithId(entity: Driver, id: Int): Driver = entity.copy(id = id)

  override val entitiesName: String = "drivers"
  override val entityName: String = "driver"

  override implicit val reads: Reads[Tables.Driver] = (
      (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "pass").read(minLength[String](10)) and
      (JsPath \ "license").read(minLength[String](10)) and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "middleName").readNullable[String] and
      (JsPath \ "phone").read[String] and
      (JsPath \ "secPhone").read[String] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "address").read[String] and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) }
    )(Driver.apply _)
  override implicit val writes: Writes[Tables.Driver] = new Writes[Driver] {
    def writes(o: Driver) = Json.obj(
      "id" -> o.id.toString,
      "pass" -> o.pass,
      "license" -> o.license,
      "lastName" -> o.lastName,
      "firstName" -> o.firstName,
      "middleName" -> o.middleName,
      "phone" -> o.phone,
      "secPhone" -> o.secPhone,
      "address" -> o.address,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)} ,
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }

  override protected def onCreateError(entity: Tables.Driver, err: Throwable): Result = {
    onChangeError(entity, err, super.onCreateError)
  }

  override protected def onUpdateError(entity: Tables.Driver, err: Throwable): Result = {
    onChangeError(entity, err, super.onUpdateError)
  }

  private def onChangeError(entity: Driver, err: Throwable, fallback: (Driver, Throwable) => Result) = {
    //todo: скорее всего по классу таблицы можно найти все поля с ограничением уникальности
    //по ним можно построить generic обработчик ошибок уникальности
    err match {
      case e: PSQLException if e.getMessage.contains("idx_pass_uq") =>
        val error = Json.obj(
          "pass" -> "Значение паспорта должно быть уникальным среди всех водителей"
        )
        UnprocessableEntity(error)
      case e: PSQLException if e.getMessage.contains("idx_license_uq") =>
        val error = Json.obj(
          "license" -> "Значение водительских прав должно быть уникальным среди всех водителей"
        )
        UnprocessableEntity(error)
      case _ => fallback(entity, err)
    }
  }
}