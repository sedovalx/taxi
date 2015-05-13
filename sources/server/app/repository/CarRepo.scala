package repository

import models.generated.Tables.{Car, CarTable}

trait CarRepo extends GenericCRUD[Car, CarTable]

