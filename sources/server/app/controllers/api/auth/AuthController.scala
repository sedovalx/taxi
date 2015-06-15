package controllers.api.auth

import javax.inject.Inject

import com.mohiva.play.silhouette.api.exceptions.NotAuthenticatedException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.api.{Environment, LoginEvent, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.{IdentityNotFoundException, InvalidPasswordException}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.api.BaseController
import models.generated.Tables.SystemUser
import org.joda.time.DateTime
import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Json, _}
import play.api.mvc.{Action, BodyParsers}
import service.auth.LoginInfoService
import utils.responses.Response

import scala.concurrent.Future

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

class AuthController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[SystemUser, JWTAuthenticator],
  accountService: LoginInfoService,
  credentialsProvider: CredentialsProvider)
  extends BaseController with Silhouette[SystemUser, JWTAuthenticator] {

  private implicit val credentialsFormat = Json.format[Credentials]

  def authenticate = Action.async(BodyParsers.parse.json) { implicit request =>
    log.debug(Json.stringify(request.body))
    // извлекаем логин/пароль из тела запроса
    request.body.validate[Credentials] map { credentials =>
      credentialsProvider.authenticate(credentials) flatMap { loginInfo =>
        // по логину извлекаем пользователя из БД
        accountService.retrieve(loginInfo) flatMap {
          // если найден, то генерируем аутентификационный токен
          case Some(user) => env.authenticatorService.create(loginInfo) flatMap { authenticator =>
            env.eventBus.publish(LoginEvent(user, request, request2Messages))
            // инициализируем аутенитфикатор
            env.authenticatorService.init(authenticator) flatMap { token =>
              // прикрепляем токен к заголовку ответа на запрос, и в тело добавляем
              env.authenticatorService.embed(token,
                Ok(Json.toJson(Token(token = token, expiresOn = authenticator.expirationDate, userId = user.id)))
              )
            }
          }
          case None => Future.failed(new NotAuthenticatedException(play.api.i18n.Messages("auth.error.wrong_credentials")))
        }
      } recoverWith {
        case e @ (_ : NotAuthenticatedException | _ : InvalidPasswordException | _ : IdentityNotFoundException) =>
          Logger.error("Ошибка аутентификации", e)
          Future { Unauthorized(Json.toJson(Response.bad(play.api.i18n.Messages("auth.error.wrong_credentials")))) }
      } recoverWith exceptionHandler
    } recoverTotal {
      case error => Future.successful(BadRequest(Response.bad("Неверный формат идентификационных данных", JsError.toJson(error))))
    }
  }

  def logOut = SecuredAction.async { implicit request =>
    env.eventBus.publish(LogoutEvent(request.identity, request, request2Messages))
    env.authenticatorService.discard(request.authenticator, Ok)
  }

  def renew = SecuredAction.async { implicit request =>
    env.authenticatorService.renew(request.authenticator, Ok)
  }
}
