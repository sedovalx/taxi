package repository

import models.generated.Tables.{Driver, DriverTable}

trait DriverRepo extends GenericCRUD[Driver, DriverTable]

class DriverRepoImpl extends DriverRepo{
  val tableQuery = DriverTable
}