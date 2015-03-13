package models.entities

import scala.slick.driver.PostgresDriver.simple._

object Role extends Enumeration {
  type Role = Value
  val Administrator = Value("Administrator")
  val Accountant = Value("Accountant")
  val Cashier = Value("Cashier")
  val Repairman = Value("Repairman")

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )
}