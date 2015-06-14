package repository

import models.generated.Tables
import models.generated.Tables.{Rent, RentFilter, RentTable}
import slick.driver.PostgresDriver

class RentRepo extends GenericCRUDImpl[Rent, RentTable, RentFilter] {
  override val tableQuery: PostgresDriver.api.TableQuery[Tables.RentTable] = RentTable
}

