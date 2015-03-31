package controllers.auth

import javax.naming.AuthenticationException

import _root_.util.responses.Response
import com.mohiva.play.silhouette.api.{LogoutEvent, LoginEvent, Silhouette}
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.BaseController
import models.entities.User
import play.api.libs.json.Json
import play.api.mvc.{BodyParsers, Action}
import utils.auth.Environment
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * This class represent token
 *
 * @param token Id of token
 * @param expiresOn The expiration time
 */
case class Token(token: String, expiresOn: DateTime, userId: Long)

/**
 * Companion object, contain format for Json
 */
object Token {


  implicit val jodaDateWrites: Writes[org.joda.time.DateTime] = new Writes[org.joda.time.DateTime] {
    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toString)
  }

  implicit val restFormat = Json.format[Token]

}

class AuthController(val env: Environment, userService: IdentityService[User])
  extends BaseController with Silhouette[User, JWTAuthenticator] {

  private implicit val credentialsFormat = Json.format[Credentials]

  def authenticate = Action.async(BodyParsers.parse.json) { implicit request =>
    // извлекаем логин/пароль из тела запроса
    request.body.validate[Credentials] map { credentials =>
      // ищем провайдера среди сконфигурированных
      (env.providers.get(CredentialsProvider.ID) match {
          // если найден, то пытаемся аутентифицировать
        case Some(p: CredentialsProvider) => p.authenticate(credentials)
          // если провайдер не найден, кидаем ошибку
        case _ => Future.failed(new AuthenticationException("Cannot find credentials provider"))
      }) flatMap { loginInfo =>
        // по логину извлекаем пользователя из БД
        userService.retrieve(loginInfo) flatMap {
          // если найден, то генерируем аутентификационный токен
          case Some(user) => env.authenticatorService.create(loginInfo) flatMap { authenticator =>
            env.eventBus.publish(LoginEvent(user, request, request2lang))
            // инициализируем аутенитфикатор
            env.authenticatorService.init(authenticator) flatMap { token =>
              // прикрепляем токен к заголовку ответа на запрос, и в тело добавляем
              env.authenticatorService.embed(token, Future.successful {
                Ok(Json.toJson(Token(token = token, expiresOn = authenticator.expirationDate, userId = user.id)))
              })
            }
          }
          case None => Future.failed(new AuthenticationException(s"Пользователь с логином ${loginInfo.providerKey} не найден."))
        }
      } recoverWith exceptionHandler
    } recoverTotal {
      case error => Future.successful(BadRequest(Response.bad("Неверный формат идентификационных данных", JsError.toFlatJson(error))))
    }
  }

  def logOut = SecuredAction.async { request =>
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang(request)))
    request.authenticator.discard(Future.successful(Ok))
  }

  def renew = SecuredAction.async { request =>
    request.authenticator.renew(Future.successful(Ok(Json.parse("{}"))))
  }

}
