package models.entities



object Role extends Enumeration {

  def toRole(role: Option[String]): Option[Role] = role.map{ r => Role.withName(r) }



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