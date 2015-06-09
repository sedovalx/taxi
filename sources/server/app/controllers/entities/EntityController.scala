package controllers.entities

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.entities.Entity
import models.generated.Tables
import models.generated.Tables.SystemUser
import play.api.db.slick.Config.driver.simple._
import play.api.libs.json._
import play.api.mvc.{BodyParsers, Result}
import repository.GenericCRUD
import scaldi.Injector
import serialization.Serializer
import service.EntityService
import utils.serialization.FormatJsError._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class EntityController[E <: Entity[E], T <: Table[E]  { val id: Column[Int] }, G <: GenericCRUD[E, T, F], F, S <: Serializer[E, F]](implicit val injector: Injector)
  extends BaseController with Silhouette[SystemUser, JWTAuthenticator] {

  protected val entityService : EntityService[E, T, G, F]
  protected val serializer: S

  import serializer._

  override protected def env: Environment[Tables.SystemUser, JWTAuthenticator] = inject [Environment[Tables.SystemUser, JWTAuthenticator]]

  protected val entityName: String //example: "driver"
  protected val entitiesName: String //example: "drivers"

  private def copyEntityWithId(entity: E, id: Int): E = entity.copyWithId(id)

  protected def onCreateInvalidJson(json: JsValue, err: JsError): Result = UnprocessableEntity(Json.toJson(err))
  protected def onUpdateInvalidJson(json: JsValue, err: JsError): Result = UnprocessableEntity(Json.toJson(err))
  protected def onCreateError(entity: E, err: Throwable): Result = BadRequest(Response.bad("Ошибка создания объекта", err.toString))
  protected def onUpdateError(entity: E, err: Throwable): Result = BadRequest(Response.bad("Ошибка обновления объекта", err.toString))

  protected def beforeCreate(json: JsValue, entity: E, identity: SystemUser): E = entity
  protected def beforeUpdate(entityId: Int, json: JsValue, entity: E, identity: SystemUser): E = copyEntityWithId(entity, entityId)
  protected def afterCreate(json: JsValue, entity: E, identity: SystemUser): E = entity
  protected def afterUpdate(json: JsValue, entity: E, identity: SystemUser): E = entity

  protected def afterSerialization(entity: Option[E], json: JsValue): JsValue = json

  private def tryParseFilter(json: JsValue): Option[F] = {
    json.validate[F] match {
      case JsSuccess(f, _) => Some(f)
      case _ => None
    }
  }

  def read = SecuredAction.async { implicit request =>
    val filterParams = request.queryString.map { case (k, v) => k -> v.mkString }
    val filterJson = Json.toJson(filterParams)
    val filter = tryParseFilter(filterJson)
    entityService.read(filter) map { entities =>
      val eJson = makeJson[List[E]](entitiesName, entities)
      Ok(eJson)
    }
  }

  def getById(id: Int) = SecuredAction.async { implicit request =>
    entityService.findById(id) map { entity =>
      val eJson = makeJson[Option[E]](entityName, entity)
      Ok(afterSerialization(entity, eJson))
    }
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = getEntityJson(request.body)
    json.validate[E] match {
      case err@JsError(_) => Future.successful(onCreateInvalidJson(json, err))
      case JsSuccess(entity, _) =>
        val toSave = beforeCreate(json, entity, request.identity)
        entityService.create(toSave, Some(request.identity.id)) map { saved =>
          val toSend = afterCreate(json, saved, request.identity)
          val eJson = makeJson(entityName, toSend)
          Ok(eJson)
        } recover {
          case e: Throwable => onCreateError(toSave, e)
        }
    }
  }

  def update(id: Int) = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = getEntityJson(request.body)
    json.validate[E] match {
      case err@JsError(_) => Future.successful(onUpdateInvalidJson(json, err))
      case JsSuccess(entity, _) =>
        val toSave = beforeUpdate(id, json, entity, request.identity)
        entityService.update (toSave, Some (request.identity.id) ) map { saved =>
          val toSend = afterUpdate(json, saved, request.identity)
          val eJson = makeJson (entityName, toSend)
          Ok (eJson)
        } recover {
          case e: Throwable => onUpdateError(toSave, e)
        }
    }
  }

  def delete(id: Int) = SecuredAction.async { request =>
    entityService.delete(id) map { wasDeleted =>
      if (wasDeleted)
        Ok(Json.obj())
      else
        NotFound(Response.bad(s"Объект с id=$id не найден"))
    }
  }

  protected def getEntityJson(json: JsValue) = json \ entityName
 }
