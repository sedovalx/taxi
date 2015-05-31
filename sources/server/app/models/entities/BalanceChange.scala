package models.entities

import java.sql.Timestamp

trait BalanceChange {
  val amount: BigDecimal
  val changeTime: Timestamp
}
