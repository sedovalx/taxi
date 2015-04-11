package utils.db.repo

import controllers.filter.UserFilter
import models.generated.Tables._
import play.api.db.slick.Config.driver.simple._

/**
 * Репозиторий учетных записей
 */
object UsersRepo extends GenericCRUD[AccountTable, Account] {
  val tableQuery = AccountTable

  def findByLogin(login: String)(implicit session: Session): Option[Account] = {
    tableQuery.filter(_.login === login).run.headOption
  }

  def isEmpty(implicit session: Session): Boolean = {
    tableQuery.length.run > 0
  }

  def find(userFilter : UserFilter)(implicit session: Session) : List[Account] = {
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
