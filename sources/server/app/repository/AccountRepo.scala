package repository

import models.entities.Role
import models.entities.Role._
import models.generated.Tables.{Account, AccountTable, AccountFilter}
import play.api.db.slick.Config.driver.simple._

trait AccountRepo extends GenericCRUD[Account, AccountTable, AccountFilter] {
  def findByLogin(login: String)(implicit session: Session): Option[Account]
  def isEmpty(implicit session: Session): Boolean
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

  /**
   * Вернуть отфильтрованных пользователей
   * @param session сессия к БД
   * @return список пользователей, попавших под фильтр
   */
  override def read(filter: Option[AccountFilter] = None)(implicit session: Session): List[Account] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        tableQuery
          .filteredBy(f.id)           (_.id === f.id.get)
          .filteredBy(f.login)        (_.login like f.login.map {s => s"%$s%" }.get)
          .filteredBy(f.lastName)     (_.lastName like f.lastName.map {s => s"%$s%" })
          .filteredBy(f.firstName)    (_.firstName like f.firstName.map {s => s"%$s%" })
          .filteredBy(f.middleName)   (_.middleName like f.middleName.map {s => s"%$s%" })
          .filteredBy(f.role)         (_.role === f.role.get)
          .query.list
    }
  }
}
