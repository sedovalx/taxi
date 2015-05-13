package service

import models.generated.Tables.{Fine, FineTable}
import repository.FineRepo

trait FineService extends EntityService[Fine, FineTable, FineRepo]

class FineServiceImpl(val repo: FineRepo) extends FineService