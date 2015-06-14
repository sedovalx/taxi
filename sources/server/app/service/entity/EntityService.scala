package service.entity

import models.entities.Entity
import repository.GenericCRUD
import slick.driver.PostgresDriver.api._
import utils.EntityNotFoundException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait EntityService[E <: Entity[E], T <: Table[E] { val id: Rep[Int] }, G <: GenericCRUD[E, T, F], F] {
  val repo: G

  protected def beforeCreate(entity: E, creatorId: Option[Int]) = Future.successful(entity.copyWithCreator(creatorId))
  protected def afterCreate(entity: E) = Future.successful(entity)
  protected def beforeUpdate(entity: E, editorId: Option[Int]) = Future.successful(entity.copyWithEditor(editorId))
  protected def afterUpdate(entity: E) = Future.successful(entity)

  def create(entity: E, creatorId: Option[Int]): Future[E] = {
    beforeCreate(entity, creatorId)
      .flatMap { entity => repo.create(entity).map(id => entity.copyWithId(id)) }
      .flatMap { entity => afterCreate(entity) }
  }

  def read(filter: Option[F]): Future[Seq[E]] = {
    repo.read(filter)
  }

  def update(entity: E, editorId: Option[Int]): Future[E] = {
    assert(entity != null, "Updated entity should not be null")
    assert(entity.id > 0, "Updated entity's id should be more then zero")

    beforeUpdate(entity, editorId)
      .flatMap { entity => repo.update(entity).map(wasFound => if (wasFound) Some(entity) else None) }
      .flatMap {
        case Some(e) => afterUpdate(e)
        case None => throw new EntityNotFoundException[E]("Id = " + entity.id, entity.getClass.getTypeName)
      }
  }

  def delete(id: Int): Future[Boolean] = {
    repo.delete(id)
  }

  def findById(id: Int): Future[Option[E]] = {
    repo.findById(id)
  }
}
