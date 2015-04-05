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
    tableQuery
      .filteredBy(userFilter.login){_.login === userFilter.login}
      .filteredBy(userFilter.lastName){_.lastName === userFilter.lastName}
      .filteredBy(userFilter.firstName){_.firstName === userFilter.firstName}
      .filteredBy(userFilter.middleName){_.middleName === userFilter.middleName}
      .filteredBy(userFilter.role){_.role === userFilter.role}
      .filteredBy(userFilter.creationDate){_.creationDate === userFilter.creationDate}
      .query.list
  }
}

