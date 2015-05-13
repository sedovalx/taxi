package service

import models.generated.Tables.{Driver, DriverTable}
import repository.DriverRepo

trait DriverService extends EntityService[Driver, DriverTable, DriverRepo]

class DriverServiceImpl(val repo: DriverRepo) extends DriverService