package repository


import controllers.filter.AccountFilter
import models.generated.Tables._
import play.api.db.slick.Config.driver.simple._


/**
 * Created by ipopkov on 14/04/15.
 */
trait CarClassRepo extends GenericCRUD[CarClass, CarClassTable]

class CarClassRepoImpl extends CarClassRepo {
  val tableQuery = CarClassTable
}


trait DriversRepo extends GenericCRUD[DriverTable, Driver]

class DriversRepoImpl extends DriversRepo{
  val tableQuery = DriverTable
}

trait AccountRepo extends GenericCRUD[AccountTable, Account] {
  def findByLogin(login: String)(implicit session: Session): Option[Account]
  def isEmpty(implicit session: Session): Boolean
  def find(userFilter : AccountFilter)(implicit session: Session) : List[Account]
}


class AccountRepoImpl extends AccountRepo {
  val tableQuery = AccountTable

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
