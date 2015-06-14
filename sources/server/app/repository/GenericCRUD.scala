package repository

import javax.inject.Inject

import models.entities.Entity
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.language.{implicitConversions, reflectiveCalls}

trait GenericCRUD[E <: Entity[E], T <: Table[E] { val id: Rep[Int] }, F] {
  protected var db: Database

  val tableQuery: TableQuery[T]

  implicit def maybeFilterConversor[X,Y](q:Query[X,Y,Seq]): MaybeFilter[X, Y] = new MaybeFilter(q)

  /*
    * Find a specific entity by id.
    */
  def findById(id: Int): Future[Option[E]] = {
    db.run(tableQuery.filter(_.id === id).result.headOption)
  }

  /**
   * Удалить пользователя
   * @param id идентификатор удаляемого пользователя
   * @return true, если пользователь был найден и удален
   */
  def delete(id: Int): Future[Boolean] = {
    db.run(tableQuery.filter(_.id === id).delete).map(_ > 0)
  }

  /**
   * Обновить данные пользователя
   * @param entity обновленные данные
   * @return true, если объект был найден, и данные обновлены
   */
  def update(entity: E): Future[Boolean] = {
    db.run(tableQuery.filter(_.id === entity.id).update(entity)).map(_ > 0)
  }

  /**
   * Создать новый объект
   * @param entity данные создаваемого объекта
   * @return id созданного объекта
   */
  def create(entity: E): Future[Int] = {
    db.run((tableQuery returning tableQuery.map(_.id)) += entity)
  }

  /**
   * Вернуть отфильтрованных пользователей
   * @return список пользователей, попавших под фильтр
   */
  def read(filter: Option[F] = None): Future[Seq[E]] = {
    db.run(tableQuery.result)
  }

  /**
   * Проверяет, есть ли элементы в репозитории
   * @return true, если репозиторий пуст
   */
  def isEmpty: Future[Boolean] = {
    db.run(tableQuery.length.result).map(_ > 0)
  }
}

abstract class GenericCRUDImpl[E <: Entity[E], T <: Table[E] { val id: Rep[Int] }, F] extends GenericCRUD[E, T, F]{
  @Inject
  protected var db: Database = null
}
