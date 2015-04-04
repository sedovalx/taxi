package utils.db.repos

import controllers.filter.UserFilter
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

  def isEmpty(implicit session: Session): Boolean = {
    tableQuery.length.run > 0
  }


  def find(userFilter : UserFilter)(implicit session: Session) : List[User] = {
      //TODO: подумать, как лучше сделать? И доделать
      val users = tableQuery.filter(u => ( userFilter.login match {
        case Some(str : String) => u.login === str
        case None => u.login.like("%%")
      }))
    /*&&
    && (userFilter.lastName match {
        case Some(str : String) => u.lastName === str
        case None => u.lastName.like("%%")
      }) &&  (userFilter.firstName match {
        case Some(str : String) => u.firstName === str
        case None => u.firstName.like("%%")
      }

        u.lastName == userFilter.lastName.getOrElse(u.lastName) &&
        u.firstName == userFilter.firstName.getOrElse(u.firstName) &&
        u.role == userFilter.lastName.getOrElse(u.role))*/
    users.list
  }
}

