package repository

import models.generated.Tables.{CarFilter, Car, CarTable}

trait CarRepo extends GenericCRUD[Car, CarTable, CarFilter]

