package repository

import models.generated.Tables
import models.generated.Tables.{Driver, DriverFilter, DriverTable}
import slick.driver.PostgresDriver

class DriverRepo extends GenericCRUDImpl[Driver, DriverTable, DriverFilter] {
  override val tableQuery: PostgresDriver.api.TableQuery[Tables.DriverTable] = DriverTable
}


