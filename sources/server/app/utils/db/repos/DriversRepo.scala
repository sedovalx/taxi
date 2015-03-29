package utils.db.repos

import models.entities.Driver
import models.tables.Drivers

import scala.slick.lifted.TableQuery

/**
 * Репозиторий водителей
 */
object DriversRepo extends GenericCRUD[Drivers, Driver]{
  val tableQuery = TableQuery[Drivers]
}