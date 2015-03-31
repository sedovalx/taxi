package controllers.entities

import java.sql.Date

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.entities.{User, Driver}
import utils.auth.{UserService, Environment}
import utils.db.repos.DriversRepo
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers}
import utils.serialization.DriverSerializer._

/**
 * Контроллер операций над водителями
 */
class DriverController(val env: Environment,
                       userService: UserService)
  extends BaseController with Silhouette[User, JWTAuthenticator]{

  /**
   * Возвращает список водителей в json-формате
   * @return
   */
  def read = SecuredAction { implicit request =>
    val drivers = withDb { session => DriversRepo.read(session) }
    val driversJson = makeJson("drivers", drivers)
    Ok(driversJson)
  }

  def getById(id: Long) = SecuredAction { implicit request =>
    val driver = withDb { session => DriversRepo.findById(id)(session) }
    val driverJson = makeJson("driver", driver)
    Ok(driverJson)
  }

  def create = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "driver"
    json.validate[Driver].fold(
      errors => BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors))),
      driver => {
        //todo: перенести установку создателя в единую точку
        val toSave: Driver = driver.copy(/*creatorId = Some(loggedIn(request).id)*/)
        val id = withDb { session => DriversRepo.create(toSave)(session) }
        val toSend = toSave.copy(id = id)
        val driverJson = makeJson("driver", toSend)
        Ok(driverJson)
      }
    )
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "driver"
    json.validate[Driver].fold(
      errors => BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors))),
      driver => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = driver.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime))/*, editorId = Some(loggedIn(request).id)*/)
        withDbAction { session => DriversRepo.update(toSave)(session) }
        val driverJson = makeJson("driver", toSave)
        Ok(driverJson)
      }
    )
  }

  def delete(id: Long) = SecuredAction { request =>
    val wasDeleted = withDb { session => DriversRepo.delete(id)(session) }
    if (wasDeleted)
      Ok(Json.parse("{}"))
    else
      NotFound(Response.bad(s"Водитель с id=$id не найден"))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
