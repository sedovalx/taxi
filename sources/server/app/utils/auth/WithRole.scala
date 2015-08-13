package utils.auth

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.entities.Role.Role
import models.generated.Tables
import models.generated.Tables.SystemUser
import play.api.i18n.Messages
import play.api.mvc.Request

import scala.concurrent.Future

/**
 * Класс проверки на принадлежность пользователя к роли
 * @param role проверяемая роль
 */
case class WithRole(role: Role) extends Authorization[SystemUser, JWTAuthenticator] {
  override def isAuthorized[B](identity: Tables.SystemUser, authenticator: JWTAuthenticator)
      (implicit request: Request[B], messages: Messages): Future[Boolean] = {
    Future.successful(identity.role == role)
  }
}
