package models.repos

import models.entities.Driver
import models.tables.Drivers

import play.api.db.slick.Config.driver.simple._

/**
 * Репозиторий пользователей системы
 */
object DriversRepo extends GenericCRUD[Drivers, Driver]{

  val tableQuery = TableQuery[Drivers]
}