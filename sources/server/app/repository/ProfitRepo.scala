package repository

import models.generated.Tables.{Profit, ProfitTable, ProfitFilter}

class ProfitRepo extends GenericCRUDImpl[Profit, ProfitTable, ProfitFilter] {
  override val tableQuery = ProfitTable
}
