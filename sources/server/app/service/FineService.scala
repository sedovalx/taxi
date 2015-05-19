package service

import models.generated.Tables.{FineFilter, Fine, FineTable}
import repository.FineRepo

trait FineService extends EntityService[Fine, FineTable, FineRepo, FineFilter]

