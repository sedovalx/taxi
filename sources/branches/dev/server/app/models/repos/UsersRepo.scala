package models.repos

import java.sql.Date

import models.entities.{Role, User}
import models.tables.Users

import scala.slick.driver.PostgresDriver.simple._

object UsersRepo extends Repository[User] {

  val objects = TableQuery[Users]

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

  override def create(entity: User)(implicit session: Session): User = ???

  override def update(entity: User)(implicit session: Session): User = ???

  override def delete(id: Long)(implicit session: Session): Boolean = ???

  override def read(implicit session: Session): List[User] = {
    objects.list
  }
}

