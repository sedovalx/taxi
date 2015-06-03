package utils

class AccountManagementException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

class AccountAlreadyExistsException(login: String) extends AccountManagementException(message = s"Пользователь с логином $login уже существует.")

class EntityNotFoundException[T](message: String, entity : String)
  extends RuntimeException(s"Entity of type $entity not found: $message")

class RunSqlException(message: String, cause: Throwable) extends RuntimeException(message, cause)