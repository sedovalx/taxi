package repository

import models.generated.Tables.{CarClass, CarClassTable}

trait CarClassRepo extends GenericCRUD[CarClass, CarClassTable]

class CarClassRepoImpl extends CarClassRepo {
  val tableQuery = CarClassTable
}
