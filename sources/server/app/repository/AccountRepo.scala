package repository

import controllers.filter.AccountFilter
import models.entities.Role
import models.entities.Role._
import models.generated.Tables.{Account, AccountTable}
import play.api.db.slick.Config.driver.simple._

trait AccountRepo extends GenericCRUD[Account, AccountTable] {
  def findByLogin(login: String)(implicit session: Session): Option[Account]
  def isEmpty(implicit session: Session): Boolean
  def find(userFilter : AccountFilter)(implicit session: Session) : List[Account]
}

class AccountRepoImpl extends AccountRepo {
  val tableQuery = AccountTable

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )

  def findByLogin(login: String)(implicit session: Session): Option[Account] = {
    tableQuery.filter(_.login === login).run.headOption
  }

  def isEmpty(implicit session: Session): Boolean = {
    tableQuery.length.run > 0
  }

  def find(userFilter : AccountFilter)(implicit session: Session) : List[Account] = {
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
