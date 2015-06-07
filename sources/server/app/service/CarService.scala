package service

import models.generated.Tables.{CarFilter, Car, CarTable}
import repository.CarRepo

trait CarService extends EntityService[Car, CarTable, CarRepo, CarFilter] {
  def getDisplayName(carId: Int): String
}

class CarServiceImpl(val repo: CarRepo) extends CarService {
  override def getDisplayName(carId: Int): String = {
    withDb { implicit session =>
      val Some(car) = repo.findById(carId)
      s"${car.model} ${car.regNumber} (${car.rate}})"
    }
  }
}


