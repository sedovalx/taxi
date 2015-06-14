package repository

import models.generated.Tables
import models.generated.Tables.{Car, CarFilter, CarTable}
import slick.driver.PostgresDriver

class CarRepo extends GenericCRUDImpl[Car, CarTable, CarFilter] {
  override val tableQuery: PostgresDriver.api.TableQuery[Tables.CarTable] = CarTable
}
