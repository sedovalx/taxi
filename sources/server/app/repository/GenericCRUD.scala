package repository

import javax.inject.Inject

import models.entities.Entity
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.language.{implicitConversions, reflectiveCalls}

trait GenericCRUD[E <: Entity[E], T <: Table[E] { val id: Rep[Int] }, F] {

  @Inject val dbConfigProvider: DatabaseConfigProvider = null
  lazy val db: Database = dbConfigProvider.get[JdbcProfile].db

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
}