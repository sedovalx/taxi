package models.entities

import java.sql.Date

import com.mohiva.play.silhouette.api.Identity
import models.entities.Role._

/**
 * Доменный класс пользователя системы
 * @param id идентификатор
 * @param login логин пользователя в системе
 * @param passwordHash хеш пароля пользователя
 * @param lastName фамилия
 * @param firstName имя
 * @param middleName отчество
 * @param role роль в системе
 */
case class User(
 id: Long,
 login: String,
 passwordHash: String,
 lastName: Option[String] = None,
 firstName: Option[String] = None,
 middleName: Option[String] = None,
 role: Role,
 creationDate: Date,
 editDate: Option[Date] = None,
 creatorId: Option[Long] = None,
 editorId: Option[Long] = None
) extends Entity with Identity

object User {
 def create(login: String, password: String, role: Role) =
  new User(id = 0, login = login, passwordHash = password, role = role, creationDate = new Date((new java.util.Date).getTime))
}