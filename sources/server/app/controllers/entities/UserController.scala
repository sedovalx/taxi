package controllers.entities

import java.sql.Date

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import controllers.{BaseController, UserAlreadyExistsException}
import models.entities.User
import models.repos.UsersRepo
import play.api.libs.json._
import play.api.mvc._
import utils.auth.Environment
import utils.extensions.SqlDate
import utils.serialization.UserSerializer._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

/**
 * Контроллер операций над пользователями
 */
class UserController(
                      val env: Environment,
                      passwordHasher: PasswordHasher,
                      authInfoService: DelegableAuthInfoService)
  extends BaseController with Silhouette[User, JWTAuthenticator] {

  /**
   * Возвращает список пользователей в json-формате
   * @return
   */
  def read = SecuredAction { implicit request =>
    val users = withDb { session => UsersRepo.read(session) }
    Ok(makeJson("users", users))
  }

  def getById(id: Long) = SecuredAction { implicit request =>
    val user = withDb { session => UsersRepo.findById(id)(session) }
    Ok(makeJson("user", user))
  }

  def create = SecuredAction.async(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => Future.successful(BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors)))),
      user => {
        //todo: перенести установку создателя в единую точку
        val toSave: User = user.copy(creatorId = Some(request.identity.id), creationDate = Some(SqlDate.now))
        createUser(toSave, env) map { createdUser =>
          Ok(makeJson("user", createdUser))
        } recover {
          case e: Throwable => BadRequest(Response.bad("Ошибка создания пользователя", e.toString))
        }
      }
    )
  }

  def update(id: Long) = SecuredAction(BodyParsers.parse.json) { request =>
    val json = request.body \ "user"
    json.validate[User].fold(
      errors => BadRequest(Response.bad("Ошибки валидации", JsError.toFlatJson(errors))),
      user => {
        // todo: перенести установку времени редактирования в единую точку
        val toSave = user.copy(id = id, editDate = Some(new Date(new java.util.Date().getTime)), editorId = Some(request.identity.id))
        withDbAction { session => UsersRepo.update(toSave)(session) }
        val userJson = makeJson("user", toSave)
        Ok(userJson)
      }
    )
  }

  def delete(id: Long) = SecuredAction { request =>
    val wasDeleted = withDb { session => UsersRepo.delete(id)(session) }
    if (wasDeleted) Ok(Json.parse("{}"))
    else NotFound(Response.bad(s"Пользователь с id=$id не найден"))
  }

  private def makeJson[T](prop: String, obj: T)(implicit tjs: Writes[T]): JsValue = JsObject(Seq(prop -> Json.toJson(obj)))

  private def createUser(user: User, env: Environment): Future[User] = {
    // тут мы должны сделать хеш пароля и сохранить его
    val loginInfo = LoginInfo(CredentialsProvider.ID, user.login)
    env.identityService.retrieve(loginInfo) flatMap {
      case None =>
        // пользователя с таким логином в БД нет
        // хешируем пароль
        val authInfo = passwordHasher.hash(user.password)
        Future {
          // сохраняем пользователя
          val id = withDb { session => UsersRepo.create(user.copy(password = ""))(session) }
          user.copy(id = id)
        } map { u =>
          // сохраняем хешированный пароль
          authInfoService.save(loginInfo, authInfo)
          u
        }
      case Some(u) =>
        // пользователь уже существует
        throw new UserAlreadyExistsException(user.login)
    }
  }
}


