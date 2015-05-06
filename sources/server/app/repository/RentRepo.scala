package repository

import models.generated.Tables.{Rent, RentTable}

trait RentRepo extends GenericCRUD[Rent, RentTable]

class RentRepoImpl extends RentRepo {
  val tableQuery = RentTable
}