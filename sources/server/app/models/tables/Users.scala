package models.tables

import models.entities.Role._
import models.entities.{Role, User}
import models.utils.TableBase
import play.api.db.slick.Config.driver.simple._
import models.mappings.RoleColumn._

/**
 * Маппинг доменной сущности на таблицу в БД
 */
class Users(tag: Tag) extends TableBase[User](tag, "user") {
  def login         = column[String]("login")
  def passwordHash  = column[String]("password_hash")
  def lastName      = column[Option[String]]("last_name")
  def firstName     = column[Option[String]]("first_name")
  def middleName    = column[Option[String]]("middle_name")
  def role          = column[Role]("role")

  def * = (id, login, passwordHash, lastName, firstName, middleName, role, creationDate, editDate, creatorId, editorId) <> ((User.apply _).tupled, User.unapply)

  def uniqueLogin = index("idx_login_uq", login, unique = true)
}

