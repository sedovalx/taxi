package service

import models.generated.Tables.{Driver, DriverTable}
import repository.DriverRepo

trait DriverService extends EntityService[Driver, DriverTable, DriverRepo]

