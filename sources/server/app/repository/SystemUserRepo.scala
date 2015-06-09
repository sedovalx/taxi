package repository

import models.entities.Role
import models.entities.Role._
import models.generated.Tables.{SystemUser, SystemUserTable, SystemUserFilter}
import play.api.db.slick.Config.driver.simple._

trait SystemUserRepo extends GenericCRUD[SystemUser, SystemUserTable, SystemUserFilter] {
  def findByLogin(login: String)(implicit session: Session): Option[SystemUser]
  def isEmpty(implicit session: Session): Boolean
}

class SystemUserRepoImpl extends SystemUserRepo {
  val tableQuery = SystemUserTable

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )

  def findByLogin(login: String)(implicit session: Session): Option[SystemUser] = {
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
  override def read(filter: Option[SystemUserFilter] = None)(implicit session: Session): List[SystemUser] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        tableQuery
          .filteredBy(f.id)           (_.id === f.id.get)
          .filteredBy(f.login)        (_.login.toLowerCase like f.login.map {s => s"%${s.toLowerCase}%" }.get)
          .filteredBy(f.lastName)     (_.lastName.toLowerCase like f.lastName.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.firstName)    (_.firstName.toLowerCase like f.firstName.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.middleName)   (_.middleName.toLowerCase like f.middleName.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.role)         (_.role === f.role.get)
          .query.list
    }
  }
}
