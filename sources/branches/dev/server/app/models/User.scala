package models

import java.sql.Date

import models.Role.Role

import scala.slick.driver.PostgresDriver.simple._

case class User(
  id: Long,
  lastName: String,
  firstName: String,
  middleName: Option[String],
  role: Role,
  creationDate: Date,
  editDate: Option[Date],
  creatorId: Option[Long],
  editorId: Option[Long]
)

class Users(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def lastName      = column[String]("last_name")
  def firstName     = column[String]("first_name")
  def middleName    = column[Option[String]]("middle_name")
  def role          = column[Role]("role")
  def creationDate  = column[Date]("creation_date")
  def editDate      = column[Option[Date]]("edit_date")
  def creatorId     = column[Option[Long]]("creator_id")
  def editorId      = column[Option[Long]]("editor_id")

  def * = (id, lastName, firstName, middleName, role, creationDate, editDate, creatorId, editorId) <> (User.tupled, User.unapply)

  def creatorRef = foreignKey("fk_user_creator", creatorId, Users.objects)(_.id)
  def editorRef = foreignKey("fk_user_editor", editorId, Users.objects)(_.id)
}

object Users {

  val objects = TableQuery[Users]

  def all(implicit session: Session): List[User] = {
    objects.list
  }

  def createAdmin(implicit session: Session): User = {
    val admin = objects.filter(u => u.role === Role.Administrator).firstOption
    admin match {
      case Some(user) => user
      case None =>
        val user = User(0, " - ", " - ", None, Role.Administrator, new Date(new java.util.Date().getTime), None, None, None)
        val userId = (objects returning objects.map(_.id)) += user
        user.copy(id = userId)
    }
  }

  def insert(user: User): User = {
    null
  }
}

