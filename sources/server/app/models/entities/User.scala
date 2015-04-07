package models.entities

import java.sql.Date

import com.mohiva.play.silhouette.api.Identity
import models.entities.Role._
import utils.extensions.DateUtils

/**
 * Доменный класс пользователя системы
 * @param id идентификатор
 * @param login логин пользователя в системе
 * @param password хеш пароля пользователя
 * @param lastName фамилия
 * @param firstName имя
 * @param middleName отчество
 * @param role роль в системе
 */
case class User(
 id: Long,
 login: String,
 password: String,
 lastName: Option[String] = None,
 firstName: Option[String] = None,
 middleName: Option[String] = None,
 role: Role,
 creationDate: Option[Date] = None,
 editDate: Option[Date] = None,
 creatorId: Option[Long] = None,
 editorId: Option[Long] = None,
 comment: Option[String] = None
) extends Entity with Identity

