package repository

import models.generated.Tables.{Rent, RentFilter, RentTable}

class RentRepo extends GenericCRUDImpl[Rent, RentTable, RentFilter] {
  override val tableQuery = RentTable
}

