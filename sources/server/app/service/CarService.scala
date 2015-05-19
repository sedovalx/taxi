package service

import models.generated.Tables.{CarFilter, Car, CarTable}
import repository.CarRepo

trait CarService extends EntityService[Car, CarTable, CarRepo, CarFilter]


