package service

import models.entities.Entity
import play.api.db.slick.Config.driver.simple._
import repository.GenericCRUD
import repository.db.DbAccessor
import utils.EntityNotFoundException
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

trait EntityService[E <: Entity[E], T <: Table[E] { val id: Column[Int] }, G <: GenericCRUD[E, T, F], F] extends DbAccessor {
  val repo: G

  protected def beforeCreate(entity: E, creatorId: Option[Int]) = Future.successful(entity.copyWithCreator(creatorId))
  protected def afterCreate(entity: E) = Future.successful(entity)
  protected def beforeUpdate(entity: E, editorId: Option[Int]) = Future.successful(entity.copyWithEditor(editorId))
  protected def afterUpdate(entity: E) = Future.successful(entity)

  def create(entity: E, creatorId: Option[Int]): Future[E] = {
    beforeCreate(entity, creatorId) flatMap { toSave =>
      val id = withDb { session => repo.create(toSave)(session) }
      afterCreate(toSave.copyWithId(id))
    }
  }

  def read(filter: Option[F]): Future[List[E]] = Future {
    withDb { session => repo.read(filter)(session) }
  }

  def update(entity: E, editorId: Option[Int]): Future[E] = {
    assert(entity != null, "Updated entity should not be null")
    assert(entity.id > 0, "Updated entity's id should be more then zero")
    beforeUpdate(entity, editorId) flatMap { toSave =>
      val wasFound = withDb { session => repo.update(toSave)(session) }
      if (!wasFound)
        throw new EntityNotFoundException[E]("Id = " + entity.id, entity.getClass.getTypeName)

      afterUpdate(toSave)
    }
  }

  def delete(id: Int): Future[Boolean] = Future {
    withDb { session => repo.delete(id)(session) }
  }

  def findById(id: Int): Future[Option[E]] = Future {
    withDb { session => repo.findById(id)(session) }
  }
}
