package utils.db

import controllers.filter.UserFilter
import models.entities.Entity
import models.generated.Tables._

trait Profile {
  val profile: scala.slick.driver.JdbcProfile
}

trait Repositories extends Profile {
  import profile.simple._

  trait GenericCRUD[T <: Table[A] with AnyRef { val id: Column[Int] }, A <: Entity] {

    val tableQuery: TableQuery[T]

    implicit def maybeFilterConversor[X,Y](q:Query[X,Y,Seq]): MaybeFilter[X, Y] = new MaybeFilter(q)

    /*
      * Find a specific entity by id.
      */
    def findById(id: Int)(implicit session: Session): Option[A] = {
      tableQuery.filter(_.id === id).run.headOption
    }

    /**
     * Удалить пользователя
     * @param id идентификатор удаляемого пользователя
     * @param session сессия к БД
     * @return true, если пользователь был найден и удален
     */
    def delete(id: Int)(implicit session: Session): Boolean = {
      tableQuery.filter(_.id === id).delete > 0
    }

    /**
     * Обновить данные пользователя
     * @param entity обновленные данные
     * @param session сессия к БД
     * @return true, если объект был найден, и данные обновлены
     */
    def update(entity: A) (implicit session: Session): Boolean = {
      tableQuery.filter(_.id === entity.id).update(entity) > 0
    }

    /**
     * Создать новый объект
     * @param entity данные создаваемого объекта
     * @param session сессия к БД
     * @return id созданного объекта
     */
    def create(entity: A) (implicit session: Session) : Int = {
      (tableQuery returning tableQuery.map(_.id)) += entity
    }

    /**
     * Вернуть отфильтрованных пользователей
     * @param session сессия к БД
     * @return список пользователей, попавших под фильтр
     */
    def read(implicit session: Session): List[A] = {
      tableQuery.list
    }
  }

  /**
   * Репозиторий водителей
   */
  object DriversRepo extends GenericCRUD[DriverTable, Driver]{
    val tableQuery = DriverTable
  }

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
}
