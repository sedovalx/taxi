package controllers.entities

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.generated.Tables._
import play.api.libs.json._
import play.api.mvc.BodyParsers
import scaldi.Injector
import service.{AccountService, DriverService}
import utils.serialization.DriverSerializer._
import utils.serialization.FormatJsError._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

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
  def read = SecuredAction.async { implicit request =>
    driverService.read map { drivers =>
      val driversJson = makeJson("drivers", drivers)
      Ok(driversJson)
    }
  }

  def getById(id: Int) = SecuredAction.async { implicit request =>
    driverService.findById(id) map { driver =>
      val driverJson = makeJson("driver", driver)
      Ok(driverJson)
    }
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "driver"
    json.validate[Driver] match {
      case err@JsError(_) => Future.successful(UnprocessableEntity(Json.toJson(err)))
      case JsSuccess(driver, _) =>
        driverService.create(driver, Some(request.identity.id)) map { saved =>
          val driverJson = makeJson("driver", saved)
          Ok(driverJson)
        }
    }
  }

  def update(id: Int) = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "driver"
    json.validate[Driver] match {
      case err@JsError(_) => Future.successful(UnprocessableEntity(Json.toJson(err)))
      case JsSuccess(driver, _) =>
        driverService.update (driver, Some (request.identity.id) ) map { _ =>
          val driverJson = makeJson ("driver", driver)
          Ok (driverJson)
        }
    }
  }

  def delete(id: Int) = SecuredAction.async { request =>
    driverService.delete(id) map { wasDeleted =>
      if (wasDeleted)
        Ok(Json.parse("{}"))
      else
        NotFound(Response.bad(s"Водитель с id=$id не найден"))
    }
  }
}
