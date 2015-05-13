package service

import models.generated.Tables.{Fine, FineTable}
import repository.FineRepo

trait FineService extends EntityService[Fine, FineTable, FineRepo]

