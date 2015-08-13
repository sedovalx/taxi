package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables._
import org.postgresql.util.PSQLException
import play.api.i18n.MessagesApi
import play.api.libs.json._
import play.api.mvc.Result
import repository.DriverRepo
import serialization.entity.DriverSerializer
import service.entity.DriverService

/**
 * Контроллер операций над водителями
 */
class DriverController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: DriverService,
  val serializer: DriverSerializer)
  extends EntityController[Driver, DriverTable, DriverRepo, DriverFilter, DriverSerializer] {

  override val entitiesName: String = "drivers"
  override val entityName: String = "driver"

  override protected def onCreateError(entityName: String, err: Throwable): Result = {
    onChangeError(entityName, err, super.onCreateError)
  }

  override protected def onUpdateError(entityId: Int, err: Throwable): Result = {
    onChangeError(entityId, err, super.onUpdateError)
  }

  private def onChangeError[T](entity: T, err: Throwable, fallback: (T, Throwable) => Result) = {
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
