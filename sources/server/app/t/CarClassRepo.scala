package t

import utils.db.repo.GenericCRUD
import models.generated.Tables.{CarClass, CarClassTable}

trait CarClassRepo extends GenericCRUD[CarClass, CarClassTable]

class CarClassRepoImpl extends CarClassRepo {
  val tableQuery = CarClassTable
}
