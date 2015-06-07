package controllers.entities

import models.generated.Tables.{Car, CarFilter, CarTable}
import repository.CarRepo
import scaldi.Injector
import serialization.CarSerializer
import service.CarService

class CarController(implicit injector: Injector)
  extends EntityController[Car, CarTable, CarRepo, CarFilter, CarSerializer]()(injector) {

  override protected val entityService = inject [CarService]
  override protected val serializer: CarSerializer = inject [CarSerializer]

  override protected val entitiesName: String = "cars"
  override protected val entityName: String = "car"

}
