package controllers

class UserManagementException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

class UserAlreadyExistsException(login: String) extends UserManagementException(message = s"Пользователь с логином $login уже существует.")