package repository

import models.generated.Tables
import models.generated.Tables.{Refund, RefundFilter, RefundTable}
import slick.driver.PostgresDriver

class RefundRepo extends GenericCRUDImpl[Refund, RefundTable, RefundFilter] {
  override val tableQuery: PostgresDriver.api.TableQuery[Tables.RefundTable] = RefundTable
}
