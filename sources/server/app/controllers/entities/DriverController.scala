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
import serialization.DriverSerializer
import service.DriverService

/**
 * Контроллер операций над водителями
 */
class DriverController(implicit injector: Injector)
  extends EntityController[Driver, DriverTable, DriverRepo, DriverFilter, DriverSerializer]()(injector) {

  override val entityService = inject [DriverService]

  override val entitiesName: String = "drivers"
  override val entityName: String = "driver"
  override protected val serializer: DriverSerializer = inject [DriverSerializer]

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