package models.repos

import models.entities.User
import models.tables.Users

import scala.slick.lifted.TableQuery

/**
 * Репозиторий пользователей системы
 */
object UsersRepo extends GenericCRUD[Users, User] {
  val tableQuery = TableQuery[Users]
}

