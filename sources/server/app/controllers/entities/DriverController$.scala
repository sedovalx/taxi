package controllers.entities

import java.sql.Date

import controllers.BaseController
import controllers.auth.AuthConfigImpl
import jp.t2v.lab.play2.auth.AuthElement
import models.repos.DriversRepo
import play.api.libs.json._
import play.api.mvc.BodyParsers
import utils.serialization.DriverSerializer._
import models.entities.Driver

/**
 * Контроллер операций над пользователями
 */
object DriverController extends BaseController  with AuthElement with AuthConfigImpl {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = StackAction(AuthorityKey -> Set()) { implicit request =>
    val drivers = withDb { session => DriversRepo.read(session) }
    val driversJson = makeJson("drivers", drivers)
    Ok(driversJson)
  }

  def getById(id: Long) = StackAction(AuthorityKey -> Set()) { implicit request =>
    val driver = withDb { session => DriversRepo.findById(id)(session) }
    val driverJson = makeJson("driver", driver)
    Ok(driverJson)
  }

  def create = StackAction(BodyParsers.parse.json, AuthorityKey -> Set()) { request =>
    val json = request.body \ "driver"
    json.validate[Driver].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      driver => {
        //todo: перенести установку создателя в единую точку
        val toSave: Driver = driver.copy(creatorId = Some(loggedIn(request).id))
        val id = withDb { session => DriversRepo.create(toSave)(session) }
        val toSend = toSave.copy(id = id)
        val driverJson = makeJson("driver", toSend)
        Ok(driverJson)
      }
    )
  }

  def update(id: Long) = StackAction(BodyParsers.parse.json, AuthorityKey -> Set()) { request =>
    val json = request.body \ "driver"
    json.validate[Driver].fold(
      errors => BadRequest(Json.obj("status" -> "Ошибки валидации", "errors" -> JsError.toFlatJson(errors))),
      driver => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = driver.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime)), editorId = Some(loggedIn(request).id))
        withDbAction { session => DriversRepo.update(toSave)(session) }
        val driverJson = makeJson("driver", toSave)
        Ok(driverJson)
      }
    )
  }

  def delete(id: Long) = StackAction(AuthorityKey -> Set()) { request =>
    val wasDeleted = withDb { session => DriversRepo.delete(id)(session) }
    if (wasDeleted)
      Ok(Json.parse("{}"))
    else
      NotFound(Json.obj("status" -> s"Водитель с id=$id не найден"))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]) = JsObject(Seq(prop -> Json.toJson(obj)))
}
