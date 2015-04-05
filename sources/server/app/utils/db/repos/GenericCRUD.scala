package utils.db.repos

/**
 * Created by ipopkov on 15/03/15.
 */

import models.entities.Entity
import models.base.GenericTable
import play.api.db.slick.Config.driver.simple._
import utils.db.MaybeFilter


trait GenericCRUD[T <: GenericTable[A], A <: Entity] extends Repository[A]{

  val tableQuery: TableQuery[T]

  implicit def maybeFilterConversor[X,Y](q:Query[X,Y,Seq]) = new MaybeFilter(q)

  /*
    * Find a specific entity by id.
    */
  def findById(id: Long)(implicit session: Session): Option[A] = {
      tableQuery.filter(_.id === id).run.headOption
  }

  /**
   * Delete a specific entity by id. If successfully completed return true, else false
   */
  def delete(id: Long)(implicit session: Session): Boolean = {
      tableQuery.filter(_.id === id).delete > 0
  }

  /**
   * Update a specific entity by id. If successfully completed return true, else false
   */
  def update(entity: A) (implicit session: Session): Boolean = {
      tableQuery.filter(_.id === entity.id).update(entity) > 0
  }

  def create(entity: A) (implicit session: Session) : Long = {
    (tableQuery returning tableQuery.map(_.id)) += entity
  }

   def read(implicit session: Session): List[A] = {
    tableQuery.list
  }
}
