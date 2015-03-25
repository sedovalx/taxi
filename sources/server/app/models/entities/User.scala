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
 lastName: Option[String],
 firstName: Option[String],
 middleName: Option[String],
 role: Role,
 creationDate: Date,
 editDate: Option[Date],
 creatorId: Option[Long],
 editorId: Option[Long]
) extends Entity with Identity