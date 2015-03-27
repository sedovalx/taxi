package models.repos

import models.entities.User
import models.tables.Users
import play.api.db.slick.Session
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.TableQuery

/**
 * Репозиторий пользователей системы
 */
object UsersRepo extends GenericCRUD[Users, User] {
  val tableQuery = TableQuery[Users]

  def findByLogin(login: String)(implicit session: Session): Option[User] = {
    tableQuery.filter(_.login === login).run.headOption
  }
}

