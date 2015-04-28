package controllers.entities

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.entities.Entity
import models.generated.Tables.{Account}
import play.api.libs.json._
import play.api.mvc.{ BodyParsers }
import repository.GenericCRUD
import service.EntityService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import play.api.db.slick.Config.driver.simple._
import serialization.FormatJsError._

/**
 * Created by ipopkov on 25/04/15.
 */
abstract class EntityController[E <: Entity, T <: Table[E]  { val id: Column[Int] }, G <: GenericCRUD[T, E]]
  extends BaseController  with Silhouette[Account, JWTAuthenticator] {

  val entityService : EntityService[E, T, G]

  implicit val reads : Reads[E]
  implicit val writes : Writes[E]


  val entityName: String //example: "driver"
  val entitiesName: String //example: "drivers"

  protected def copyEntityWithId(entity: E): E

  def read = SecuredAction.async { implicit request =>
    entityService.read map { entities =>
      val eJson = makeJson[List[E]](entitiesName, entities)
      Ok(eJson)
    }
  }

  def getById(id: Int) = SecuredAction.async { implicit request =>
    entityService.findById(id) map { entity =>
      val eJson = makeJson[Option[E]](entityName, entity)
      Ok(eJson)
    }
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ entityName
    json.validate[E] match {
      case err@JsError(_) => Future.successful(UnprocessableEntity(Json.toJson(err)))
      case JsSuccess(entity, _) =>
        entityService.create(entity, Some(request.identity.id)) map { saved =>
          val eJson = makeJson(entityName, saved)
          Ok(eJson)
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка создания объекта", e.toString))
        }
    }
  }

  def update(id: Int) = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ entityName
    json.validate[E] match {
      case err@JsError(_) => Future.successful(UnprocessableEntity(Json.toJson(err)))
      case JsSuccess(entity, _) =>
        val toSave = copyEntityWithId(entity)
        entityService.update (toSave, Some (request.identity.id) ) map { saved =>
          val eJson = makeJson (entityName, saved)
          Ok (eJson)
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка обновления объекта", e.toString))
        }
    }
  }

  def delete(id: Int) = SecuredAction.async { request =>
    entityService.delete(id) map { wasDeleted =>
      if (wasDeleted)
        Ok(Json.parse("{}"))
      else
        NotFound(Response.bad(s"Объект с id=$id не найден"))
    }
  }
 }
