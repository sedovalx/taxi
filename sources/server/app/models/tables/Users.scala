package models.tables

import java.sql.Date
import models.entities.Role._
import models.entities.User
import models.repos.UsersRepo
import scala.slick.driver.PostgresDriver.simple._

/**
 * Маппинг доменной сущности на таблицу в БД
 */
class Users(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def login         = column[String]("login")
  def password      = column[String]("password")
  def lastName      = column[Option[String]]("last_name")
  def firstName     = column[Option[String]]("first_name")
  def middleName    = column[Option[String]]("middle_name")
  def role          = column[Role]("role")
  def creationDate  = column[Date]("creation_date")
  def editDate      = column[Option[Date]]("edit_date")
  def creatorId     = column[Option[Long]]("creator_id")
  def editorId      = column[Option[Long]]("editor_id")

  def * = (id, login, password, lastName, firstName, middleName, role, creationDate, editDate, creatorId, editorId) <> (User.tupled, User.unapply)

  def creatorRef = foreignKey("fk_user_creator", creatorId, UsersRepo.objects)(_.id)
  def editorRef = foreignKey("fk_user_editor", editorId, UsersRepo.objects)(_.id)

  def uniqueLogin = index("idx_login_uq", login, unique = true)
}