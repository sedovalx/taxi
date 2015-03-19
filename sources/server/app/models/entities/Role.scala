package models.entities

import play.api.db.slick.Config.driver.simple._

object Role extends Enumeration {
  type Role = Value
  // администратор
  val Administrator = Value("Administrator")
  // бухгалтер
  val Accountant = Value("Accountant")
  // кассир
  val Cashier = Value("Cashier")
  // ремонтник
  val Repairman = Value("Repairman")

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )
}