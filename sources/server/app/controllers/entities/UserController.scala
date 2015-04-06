package controllers.entities

import java.sql.Date

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import controllers.filter.UserFilter
import models.entities.{Role, User}
import play.api.libs.json._
import play.api.mvc._
import utils.auth.{Environment, UserService}
import utils.db.repos.UsersRepo
import utils.extensions.DateUtils
import utils.serialization.UserSerializer._
import utils.serialization.FormatJsError._
import DateUtils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * Контроллер операций над пользователями
 */
class UserController(val env: Environment,
                      userService: UserService)
  extends BaseController with Silhouette[User, JWTAuthenticator] {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = SecuredAction { implicit request =>
    val userFilterOpt = parseUserFilterFromQueryString(request)
    var users = List[User]()
    withDb { session =>
      userFilterOpt match {
        case Some(userFilter: UserFilter) => users = UsersRepo.find(userFilter)(session)
        case _ => users = UsersRepo.read(session)
    }
    }
    //val users = withDb { session => UsersRepo.read(session) }
    Ok(makeJson("users", users))
  }

  def getById(id: Long) = SecuredAction { implicit request =>
    val user = withDb { session => UsersRepo.findById(id)(session) }
    Ok(makeJson("user", user))
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User] match {
      case JsSuccess(user, _) => {
        val toSave: User = user.copy(creatorId = Some(request.identity.id), creationDate = Some(DateUtils.now))
        userService.createUser(toSave) map { createdUser =>
          Ok(makeJson("user", createdUser))
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка создания пользователя", e.toString))
        }
      }
      case err@JsError(_) => Future(BadRequest(Json.toJson(err)))
    }
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"

    json.validate[User] match {
      case JsSuccess(user, _) => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = user.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime)), editorId = Some(request.identity.id))
        withDbAction { session => UsersRepo.update(toSave)(session) }
        val userJson = makeJson("user", toSave)
        Ok(userJson)
      }
      case err@JsError(_) => BadRequest(Json.toJson(err))
    }
  }

  def delete(id: Long) = SecuredAction { request =>
    val wasDeleted = withDb { session => UsersRepo.delete(id)(session) }
    if (wasDeleted) Ok(Json.parse("{}"))
    else NotFound(Response.bad(s"Пользователь с id=$id не найден"))
  }

  def currentUser = SecuredAction { request =>
    val user = request.identity
    Ok(makeJson("user", user))
  }


  def parseUserFilterFromQueryString(implicit request:RequestHeader) : Option[UserFilter] = {
    val query = request.queryString.map { case (k, v) => k -> v.mkString }
    val login = query get ("login")
    val lastName = query get ("lastName")
    val firstName = query get ("firstName")
    val middleName = query get ("middleName")
    var role = Role.toRole(query get ("role") )
    //TODO: уточнить формат даты
    val createDate = stringToDate ( query get ("createDate") )

    var filter = new UserFilter( login, lastName, firstName, middleName, role, createDate )

    //TODO: вынести в utils?
    var fieldList = filter.productIterator.toList.collect ({ case Some(x) => x } );
    val hasAny = fieldList.length > 0

    hasAny match {
      case true => Some(filter)
      case _ => None
    }
  }


  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]): JsValue = JsObject(Seq(prop -> Json.toJson(obj)))
}


