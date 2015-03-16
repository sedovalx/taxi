package models.repos

/**
 * Created by ipopkov on 15/03/15.
 */

import models.entities.Entity
import models.utils.GenericTable
import play.api.Play.current
import play.api.db.DB
import play.api.db.slick.Config.driver
import play.api.db.slick.Config.driver.simple._


abstract class GenericCRUD[T <: GenericTable[A], A <: Entity] extends Repository[A]{

  val tableQuery: TableQuery[T]

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
  def updateById(id: Long, entity: A) (implicit session: Session) = {
      tableQuery.update(entity) > 0
  }

  def insert(entity: A) (implicit session: Session) : Long = {
    val idFromDb = (tableQuery returning tableQuery.map(_.id)) += entity
    idFromDb
  }

   def read(implicit session: Session): List[A] = {
    tableQuery.list
  }


}