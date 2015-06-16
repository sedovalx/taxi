package models.entities

object AccountType extends Enumeration {
  type AccountType = Value

  // Арендный
  val Rent = Value("Rent")
  // Ремонтный
  val Repair = Value("Repair")
  // Штрафной
  val Fine = Value("Fine")
}
