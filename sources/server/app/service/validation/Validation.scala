package service.validation

import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.json._

import scala.language.postfixOps

trait ValidationError {
  def message: String
}

class CommonValidationError(val message: String) extends ValidationError

class PropertyValidationError(val propertyName: String, val errors: Seq[String]) extends ValidationError {
  def this(propertyName: String, error: String) = this(propertyName, Seq(error))

  def message: String = Map(propertyName -> errors).toString()
}

trait ValidationResult

class SuccessfulResult extends ValidationResult

class NotAcceptableActionResult(val reason: String) extends ValidationResult

class ValidationErrorsResult(val errors: Seq[ValidationError]) extends ValidationResult {
  def this(error: ValidationError) = this(Seq(error))
}

object ValidationResult {
  def propertyError(propertyName: String, error: String): ValidationResult =
    propertyError(new PropertyValidationError(propertyName, error))

  def propertyError(error: PropertyValidationError): ValidationResult =
    new ValidationErrorsResult(error)

  def success(): ValidationResult =
    new SuccessfulResult

  def notAcceptable(reason: String): ValidationResult =
    new NotAcceptableActionResult(reason)
}

abstract class ValidationException(val message: String) extends RuntimeException(message)

class ValidationErrorsException(val entityType: String, val errors: Seq[ValidationError])
  extends ValidationException(play.api.i18n.Messages("exception.validation.entity.invalid", entityType, errors map { _.message }))

class NotAcceptableActionException(val reason: String)
  extends ValidationException(play.api.i18n.Messages("exception.validation.action.not-allowed", reason))

trait Validation {
  def assertValidationResult(entityType: String, result: ValidationResult): Unit = {
    result match {
      case r: ValidationErrorsResult =>
        throw new ValidationErrorsException(entityType, r.errors)
      case r: NotAcceptableActionResult =>
        throw new NotAcceptableActionException(r.reason)
      case _ =>
    }
  }
}

object Validation {
  implicit val validationErrorWrites: Writes[Seq[ValidationError]] = new Writes[Seq[ValidationError]] {
    override def writes(errors: Seq[ValidationError]): JsValue = {
      val propertyErrors = errors collect { case e: PropertyValidationError => e }
      val commonErrors = errors filter { e => !e.isInstanceOf[PropertyValidationError] }
      if (commonErrors.nonEmpty)
        Json.obj("errors" -> commonErrors.foldLeft(JsArray()){(seed, e) =>
          seed.+:(JsString(e.message))
        })
      else
        propertyErrors.foldLeft(Json.obj())((seed, e) => seed + (e.propertyName, Json.toJson(e.errors)))
    }
  }
}



//case class PropertyValidationError(propertyName: String, errors: Seq[String])
//
//case class ObjectValidationError(errors: Seq[String])
//
//case class ValidationError(
//  entityType: String,
//  propertyErrors: Option[Seq[PropertyValidationError]],
//  objectErrors: Option[Seq[ObjectValidationError]]){
//
//  def propertyErrorsToMap(): Map[String, Seq[String]] = {
//    propertyErrors map { e => (e.propertyName, e.errors) } toMap
//  }
//}
//
//object ValidationError {
//  def single(entityType: String, error: PropertyValidationError): ValidationError = ValidationError(entityType, Seq(error))
//  def single(entityType: String, propertyName: String, error: String): ValidationError = {
//    val propertyError = PropertyValidationError.single(propertyName, error)
//    ValidationError.single(entityType, propertyError)
//  }
//}
//
//object PropertyValidationError {
//  def single(propertyName: String, error: String) = PropertyValidationError(propertyName, Seq(error))
//}
//
//class EntityValidationException(val error: ValidationError)
//  extends RuntimeException(play.api.i18n.Messages("exception.entity.validation", error.entityType, error.propertyErrorsToMap()))
