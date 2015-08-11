package utils.validation

import models.entities.Entity
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

case class PropertyValidationError(propertyName: String, errors: Seq[String])

case class ValidationError(entityType: String, errors: Seq[PropertyValidationError]){
  def errorsToMap(): Map[String, Seq[String]] = {
    errors map { e => (e.propertyName, e.errors) } toMap
  }
}

object ValidationError {
  def single(entityType: String, error: PropertyValidationError): ValidationError = ValidationError(entityType, Seq(error))
  def single(entityType: String, propertyName: String, error: String): ValidationError = {
    val propertyError = PropertyValidationError.single(propertyName, error)
    ValidationError.single(entityType, propertyError)
  }
}

object PropertyValidationError {
  def single(propertyName: String, error: String) = PropertyValidationError(propertyName, Seq(error))
}

class EntityValidationException(val error: ValidationError)
  extends RuntimeException(play.api.i18n.Messages("exception.entity.validation", error.entityType, error.errorsToMap()))

trait Validation[E <: Entity[E]] {
  /**
   * Валидирует переданный объект и бросает исключение, если что-то не так
   * @param entity валидируемый объект
   * @param producerId пользователь, изменивший объект
   * @throws EntityValidationException если обнаружены ошибки валидации
   */
  protected def validate(entity: E, producerId: Option[Int]): Future[Option[ValidationError]] = Future.successful(None)

  protected def throwIfInInvalidState(entity: E, producerId: Option[Int]): Future[Unit] = {
    validate(entity, producerId) map {
      case Some(error) => throw new EntityValidationException(error)
      case None =>
    }
  }
}
