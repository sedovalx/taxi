package repository

import models.generated.Tables.{Fine, FineTable}

trait FineRepo extends GenericCRUD[Fine, FineTable]


