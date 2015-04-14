package service

import models.entities.Entity
import play.api.db.slick.Config.driver.simple._
import repository.GenericCRUD
import t.EntityNotFoundException
import repository.db.DbAccessor

trait EntityService[E <: Entity, T <: Table[E], G <: GenericCRUD[T, E]] extends DbAccessor {
  val repo: G
  def setCreatorAndDate(entity: E, creatorId: Int): E
  def setEditorAndDate(entity: E, editorId: Int): E
  def setId(entity: E, id: Int): E

  def create(entity: E, creatorId: Int): E = {
    val toSave = setCreatorAndDate(entity, creatorId)
    val id = withDb { session => repo.create(toSave)(session) }
    setId(entity, id)
  }

  def read: List[E] = {
    withDb { session => repo.read(session) }
  }

  def update(entity: E, editorId: Int): E = {
    val toSave = setEditorAndDate(entity, editorId)
    withDb { session => repo.update(toSave)(session) } match {
      case true => toSave
      case _ => throw new EntityNotFoundException[E]("Id = " + entity.id)
    }
  }

  def delete(id: Int): Boolean =
    withDb { session => repo.delete(id)(session) }

  def findById(id: Int): Option[E] =
    withDb { session => repo.findById(id)(session) }
}
