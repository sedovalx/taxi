package repository

import models.generated.Tables.{Driver, DriverTable}

trait DriverRepo extends GenericCRUD[Driver, DriverTable]

