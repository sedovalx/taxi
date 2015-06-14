package service.entity

import models.generated.Tables.{DriverFilter, Driver, DriverTable}
import repository.DriverRepo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DriverService extends EntityService[Driver, DriverTable, DriverRepo, DriverFilter] {
  def getDisplayName(driverId: Int): Future[String]
}

class DriverServiceImpl(val repo: DriverRepo) extends DriverService {
  override def getDisplayName(driverId: Int): Future[String] = {
    repo.findById(driverId) map {
      case Some(driver) =>
        s"${driver.lastName} ${driver.firstName}" +
          (driver.middleName match {
            case Some(s) => " " + s
            case None => ""
          })
      case None => "None"
    }
  }
}

