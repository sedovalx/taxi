package models.repos

import java.sql.Date

import models.entities.{Role, User}
import models.tables.Users

import scala.slick.driver.PostgresDriver.simple._

/**
 * Репозиторий пользователей системы
 */
object UsersRepo extends Repository[User] {

  val objects = TableQuery[Users]

  /**
   * Создать запись пользователяс правами администратора
   * @param session сессия к БД
   * @return созданный пользователь
   */
  def createAdmin(implicit session: Session): User = {
    val admin = objects.filter(u => u.role === Role.Administrator).firstOption
    admin match {
      case Some(user) => user
      case None =>
        val user = User(0, "admin", "", None, None, None, Role.Administrator, new Date(new java.util.Date().getTime), None, None, None)
        val userId = (objects returning objects.map(_.id)) += user
        user.copy(id = userId)
    }
  }

  /**
   * Получить пользователя по идентификатору
   * @param id идентификатор пользователя
   * @return найденный пользователь
   */
  def getById(id: Long)(implicit session: Session): Option[User] = {
    objects.filter(_.id === id).run.headOption
  }

  //todo: убрать отсюда - клиентский код должен вызывать метод read с параметрами фильтрации
  def authenticate(login: String, password: String)(implicit session: Session): Option[User] = {
    objects.filter(u => u.login === login && u.password === password).run.headOption
  }

  override def create(entity: User)(implicit session: Session): User = ???

  override def update(entity: User)(implicit session: Session): User = ???

  override def delete(id: Long)(implicit session: Session): Boolean = ???

  override def read(implicit session: Session): List[User] = {
    objects.list
  }
}

