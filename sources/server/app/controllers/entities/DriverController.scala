package controllers.entities

import java.sql.Date

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.generated.Tables._
import play.api.libs.json._
import play.api.mvc.BodyParsers
import scaldi.Injector
import service.{DriverService, AccountService}
import utils.serialization.DriverSerializer._

/**
 * Контроллер операций над водителями
 */
class DriverController(implicit inj: Injector) extends BaseController with Silhouette[Account, JWTAuthenticator] {

  implicit val env = inject [Environment[Account, JWTAuthenticator]]
  implicit val accountService = inject[AccountService]
  implicit val driverService = inject[DriverService]

  /**
   * Возвращает список водителей в json-формате
   * @return
   */
  def read = SecuredAction { implicit request =>
    val drivers = driverService.read
    val driversJson = makeJson("drivers", drivers)
    Ok(driversJson)
  }

  def getById(id: Int) = SecuredAction { implicit request =>
    val driver = driverService.findById(id)
    val driverJson = makeJson("driver", driver)
    Ok(driverJson)
  }

  def create = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "driver"
    json.validate[Driver].fold(
      errors => BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors))),
      driver => {
        //todo: перенести установку создателя в единую точку
        val toSave: Driver = driver.copy(creatorId = Some(request.identity.id))
        val savedDriver = driverService.create(toSave, request.identity.id)
        val toSend = savedDriver.copy(id = driver.id)
        val driverJson = makeJson("driver", toSend)
        Ok(driverJson)
      }
    )
  }

  def update(id: Int) = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "driver"
    json.validate[Driver].fold(
      errors => BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors))),
      driver => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = driver.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime)), editorId = Some(request.identity.id))
        driverService.update(toSave, request.identity.id)
        val driverJson = makeJson("driver", toSave)
        Ok(driverJson)
      }
    )
  }

  def delete(id: Int) = SecuredAction { request =>
    val wasDeleted = driverService.delete(id)
    if (wasDeleted)
      Ok(Json.parse("{}"))
    else
      NotFound(Response.bad(s"Водитель с id=$id не найден"))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
