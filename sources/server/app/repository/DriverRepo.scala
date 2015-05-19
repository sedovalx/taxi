package repository

import models.generated.Tables.{DriverFilter, Driver, DriverTable}

trait DriverRepo extends GenericCRUD[Driver, DriverTable, DriverFilter]

