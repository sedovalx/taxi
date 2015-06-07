package service

import models.generated.Tables.{DriverFilter, Driver, DriverTable}
import repository.DriverRepo

trait DriverService extends EntityService[Driver, DriverTable, DriverRepo, DriverFilter] {
  def getDisplayName(driverId: Int): String
}

class DriverServiceImpl(val repo: DriverRepo) extends DriverService {
  override def getDisplayName(driverId: Int): String = {
    withDb { implicit session =>
      val Some(driver) = repo.findById(driverId)
      s"${driver.lastName} ${driver.firstName}" +
        (driver.middleName match {
          case Some(s) => " " + s
          case None => ""
        })
    }
  }
}

