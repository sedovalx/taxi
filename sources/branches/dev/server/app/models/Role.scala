package models

import scala.slick.driver.PostgresDriver.simple._
//sealed trait Role
//
//object Role {
//
//  case object Administrator extends Role
//  case object Accountant extends Role
//  case object Cashier extends Role
//  case object Repairman extends Role
//
//  implicit def Role2Int(role: Role): Int = {
//    role match {
//      case Administrator => 1
//      case Accountant => 2
//      case Cashier => 4
//      case Repairman => 8
//      case _ => throw new NotImplementedError(s"Conversion from $role to Int is not supported yet.")
//    }
//  }
//
//  implicit def Int2Role(i: Int): Role = {
//    i match {
//      case 1 => Administrator
//      case 2 => Accountant
//      case 4 => Cashier
//      case 8 => Repairman
//      case _ => throw new NotImplementedError(s"Not supported role value.")
//    }
//  }
//
//  implicit val roleColumnType = MappedColumnType.base[Role, Int]( { r => r }, { i => i } )
//}

object Role extends Enumeration {
  type Role = Value
  val Administrator = Value("Administrator")
  val Accountant = Value("Accountant")
  val Cashier = Value("Cashier")
  val Repairman = Value("Repairman")

  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )
}