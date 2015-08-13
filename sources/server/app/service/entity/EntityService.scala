package service.entity

import models.entities.Entity
import repository.GenericCRUD
import service.validation.{Validation, ValidationException, ValidationResult}
import slick.driver.PostgresDriver.api._
import utils.EntityNotFoundException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait EntityService[E <: Entity[E], T <: Table[E] { val id: Rep[Int] }, G <: GenericCRUD[E, T, F], F] extends Validation {
  val repo: G

  /**
   * Валидирует переданный объект и бросает исключение, если что-то не так
   * @param entity валидируемый объект
   * @param performerId пользователь, изменивший объект
   * @throws ValidationException если обнаружены ошибки валидации
   */
  protected def validate(entity: E, performerId: Option[Int]): Future[ValidationResult] = Future.successful(ValidationResult.success())
  protected def validateBeforeCreate(entity: E, performerId: Option[Int]): Future[ValidationResult] = validate(entity, performerId)
  protected def validateBeforeUpdate(entity: E, performerId: Int): Future[ValidationResult] = validate(entity, Some(performerId))
  protected def validateBeforeDelete(entity: E, performerId: Int): Future[ValidationResult] = validate(entity, Some(performerId))

  protected def beforeCreate(entity: E, creatorId: Option[Int]): Future[E] = {
    val copy = entity.copyWithCreator(creatorId)
    validateBeforeCreate(copy, creatorId) map { r => assertValidationResult(entity.getClass.getName, r) } map { _ => copy }
  }

  protected def beforeUpdate(entity: E, editorId: Int): Future[E] = {
    val copy = entity.copyWithEditor(Some(editorId))
    validateBeforeUpdate(copy, editorId) map { r => assertValidationResult(entity.getClass.getName, r) } map { _ => copy }
  }

  protected def beforeDelete(id: Int, performerId: Int): Future[Unit] = {
    repo.findById(id) flatMap {
      case Some(entity) => validateBeforeDelete(entity, performerId) map { r => assertValidationResult(entity.getClass.getName, r) }
      case None => Future.successful(Unit)
    }
  }

  protected def afterCreate(entity: E): Future[E] = Future.successful(entity)
  protected def afterUpdate(entity: E): Future[E] = Future.successful(entity)

  def create(entity: E, creatorId: Option[Int]): Future[E] = {
    beforeCreate(entity, creatorId)
      .flatMap { entity => repo.create(entity).map(id => entity.copyWithId(id)) }
      .flatMap { afterCreate }
  }

  def create(entity: E, creatorId: Int): Future[E] = create(entity, Some(creatorId))

  def read(filter: Option[F]): Future[Seq[E]] = {
    repo.read(filter)
  }

  def update(entity: E, editorId: Int): Future[E] = {
    assert(entity != null, "Updated entity should not be null")
    assert(entity.id > 0, "Updated entity's id should be more then zero")

    beforeUpdate(entity, editorId)
      .flatMap { entity => repo.update(entity).map(wasFound => if (wasFound) Some(entity) else None) }
      .flatMap {
        case Some(e) => afterUpdate(e)
        case None => throw new EntityNotFoundException[E]("Id = " + entity.id, entity.getClass.getTypeName)
      }
  }

  def delete(id: Int, performerId: Int): Future[Boolean] = {
    beforeDelete(id, performerId) flatMap { _ => repo.delete(id) }
  }

  def findById(id: Int): Future[Option[E]] = {
    repo.findById(id)
  }
}
