package models.entities

import play.api.db.slick.Config.driver.simple._

object Role extends Enumeration {

  def toRole(role: Option[String]): Option[Role] ={
    role match {
      case Some(s: String) => Some(Role.withName(s))
      case None => None
    }
  }

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )

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