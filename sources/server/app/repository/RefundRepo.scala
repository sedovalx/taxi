package repository

import models.generated.Tables.{Refund, RefundFilter, RefundTable}

class RefundRepo extends GenericCRUDImpl[Refund, RefundTable, RefundFilter] {
  override val tableQuery = RefundTable
}


