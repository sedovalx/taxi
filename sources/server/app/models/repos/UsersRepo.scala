package models.repos

import java.sql.Date

import models.entities.{Role, User}
import models.tables.Users
import models.mappings.RoleColumn._

import play.api.db.slick.Config.driver.simple._

/**
 * Репозиторий пользователей системы
 */
object UsersRepo extends GenericCRUD[Users, User]{

   val tableQuery = TableQuery[Users]

  /**
   * Создать запись пользователяс правами администратора
   * @param session сессия к БД
   * @return созданный пользователь
   */
  def createAdmin(implicit session: Session): User = {
    val admin = tableQuery.filter(u => u.role === Role.Administrator).firstOption
    admin match {
      case Some(user) => user
      case None =>
        val user = User(0, "admin", "", None, None, None, Role.Administrator, new Date(new java.util.Date().getTime), None, None, None)
        val userId = (tableQuery returning tableQuery.map(_.id)) += user
        user.copy(id = userId)
    }
  }

  //todo: убрать отсюда - клиентский код должен вызывать метод read с параметрами фильтрации
  def authenticate(login: String, password: String)(implicit session: Session): Option[User] = {
    tableQuery.filter(u => u.login === login && u.password === password).run.headOption
  }
}

