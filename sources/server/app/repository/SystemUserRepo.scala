package repository

import models.entities.Role
import models.entities.Role._
import models.generated.Tables
import models.generated.Tables.{SystemUser, SystemUserTable, SystemUserFilter}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SystemUserRepo extends GenericCRUD[SystemUser, SystemUserTable, SystemUserFilter] {

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )

  def findByLogin(login: String): Future[Option[SystemUser]] = {
    db.run(tableQuery.filter(_.login === login).result.headOption)
  }

  /**
   * Вернуть отфильтрованных пользователей
   * @return список пользователей, попавших под фильтр
   */
  override def read(filter: Option[Tables.SystemUserFilter]): Future[Seq[Tables.SystemUser]] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        val query = tableQuery
          .filteredBy(f.id)           (_.id.? === f.id)
          .filteredBy(f.login)        (_.login.toLowerCase.? like f.login.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.lastName)     (_.lastName.toLowerCase like f.lastName.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.firstName)    (_.firstName.toLowerCase like f.firstName.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.middleName)   (_.middleName.toLowerCase like f.middleName.map {s => s"%${s.toLowerCase}%" })
          .filteredBy(f.role)         (_.role.? === f.role)
          .query
        db.run(query.result)
    }
  }

  override val tableQuery: PostgresDriver.api.TableQuery[Tables.SystemUserTable] = SystemUserTable
}
