package utils

class AccountManagementException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

class AccountAlreadyExistsException(login: String) extends AccountManagementException(message = s"Пользователь с логином $login уже существует.")