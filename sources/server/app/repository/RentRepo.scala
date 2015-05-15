package repository

import models.generated.Tables.{RentFilter, Rent, RentTable}

trait RentRepo extends GenericCRUD[Rent, RentTable, RentFilter]

