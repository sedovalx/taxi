package service

import models.generated.Tables.{Car, CarTable}
import repository.CarRepo

trait CarService extends EntityService[Car, CarTable, CarRepo]

class CarServiceImpl(val repo: CarRepo) extends CarService
