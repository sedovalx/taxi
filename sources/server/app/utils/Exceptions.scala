package utils

import play.api.i18n.Messages.Implicits._
import play.api.Play.current

class AccountManagementException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)

class AccountAlreadyExistsException(login: String)
  extends AccountManagementException(message = play.api.i18n.Messages("exception.login.already-exists", login))

class EntityNotFoundException[T](message: String, entity : String)
  extends RuntimeException(play.api.i18n.Messages("exception.entity.not-found", entity, message))

class RunSqlException(message: String, cause: Throwable)
  extends RuntimeException(message, cause)

class EntityJsonFormatException(entityType: String, message: String)
  extends RuntimeException(play.api.i18n.Messages("exception.entity.json-wrong-format", entityType, message))

class EntityJsonRootMissingException(entityType: String)
  extends EntityJsonFormatException(entityType, play.api.i18n.Messages("exception.entity.json-root-missing"))

class EntityValidationException(val entityType: String, val errors: Map[String, Seq[String]])
  extends RuntimeException(play.api.i18n.Messages("exception.entity.validation", entityType, errors))
