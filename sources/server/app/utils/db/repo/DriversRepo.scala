package utils.db.repo

import models.generated.Tables._

/**
 * Репозиторий водителей
 */
object DriversRepo extends GenericCRUD[DriverTable, Driver]{
  val tableQuery = DriverTable
}
