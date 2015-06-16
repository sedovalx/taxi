package service.entity

import javax.inject.Inject

import models.generated.Tables.{CarFilter, Car, CarTable}
import repository.CarRepo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait CarService extends EntityService[Car, CarTable, CarRepo, CarFilter] {
  def getDisplayName(carId: Int): Future[String]
}

class CarServiceImpl @Inject() (val repo: CarRepo) extends CarService {
  override def getDisplayName(carId: Int): Future[String] = {
    repo.findById(carId) map {
      case Some(car) => s"${car.model} ${car.regNumber} (${car.rate}})"
      case None => "None"
    }
  }
}


