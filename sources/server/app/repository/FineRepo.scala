package repository

import models.generated.Tables.{FineFilter, Fine, FineTable}

trait FineRepo extends GenericCRUD[Fine, FineTable, FineFilter]


