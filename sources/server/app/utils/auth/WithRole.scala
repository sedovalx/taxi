package utils.auth

import com.mohiva.play.silhouette.api.Authorization
import models.entities.Role.Role
import models.entities.User
import play.api.i18n.Lang
import play.api.mvc.RequestHeader

/**
 * Класс проверки на принадлежность пользователя к роли
 * @param role проверяемая роль
 */
case class WithRole(role: Role) extends Authorization[User] {
  def isAuthorized(identity: User)(implicit request: RequestHeader, lang: Lang) = identity.role == role
}
