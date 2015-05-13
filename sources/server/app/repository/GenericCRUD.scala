package repository

import models.entities.Entity
import play.api.db.slick.Config.driver.simple._
import utils.db.MaybeFilter

import scala.language.{implicitConversions, reflectiveCalls}

trait GenericCRUD[E <: Entity[E], T <: Table[E] { val id: Column[Int] }] {

  val tableQuery: TableQuery[T]

  implicit def maybeFilterConversor[X,Y](q:Query[X,Y,Seq]): MaybeFilter[X, Y] = new MaybeFilter(q)

  /*
    * Find a specific entity by id.
    */
  def findById(id: Int)(implicit session: Session): Option[E] = {
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
  def update(entity: E) (implicit session: Session): Boolean = {
    tableQuery.filter(_.id === entity.id).update(entity) > 0
  }

  /**
   * Создать новый объект
   * @param entity данные создаваемого объекта
   * @param session сессия к БД
   * @return id созданного объекта
   */
  def create(entity: E) (implicit session: Session) : Int = {
    (tableQuery returning tableQuery.map(_.id)) += entity
  }

  /**
   * Вернуть отфильтрованных пользователей
   * @param session сессия к БД
   * @return список пользователей, попавших под фильтр
   */
  def read(implicit session: Session): List[E] = {
    tableQuery.list
  }
}




