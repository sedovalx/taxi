package models.entities

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
}