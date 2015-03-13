package models.repos

import models.entities.Entity
import scala.slick.driver.PostgresDriver.simple._

abstract class Repository[T <: Entity] {
  def create(entity: T)(implicit session: Session): T
  def read(implicit session: Session): List[T]
  def update(entity: T)(implicit session: Session): T
  def delete(id: Long)(implicit session: Session): Boolean
}
