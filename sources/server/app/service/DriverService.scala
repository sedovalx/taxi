package service

import models.generated.Tables.{DriverFilter, Driver, DriverTable}
import repository.DriverRepo

trait DriverService extends EntityService[Driver, DriverTable, DriverRepo, DriverFilter]

