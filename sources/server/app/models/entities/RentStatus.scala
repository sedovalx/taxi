package models.entities

object RentStatus extends Enumeration {
  def toRentStatus(status: Option[String]) = status.map { s => RentStatus.withName(s) }

  type RentStatus = Value
  // Активна
  val Active = Value("Active")
  // Приостановлена
  val Suspended = Value("Suspended")
  // Под расчет
  val SettlingUp = Value("SettlingUp")
  // Закрыта
  val Closed = Value("Closed")
}
