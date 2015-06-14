package db

import models.entities.AccountType.AccountType
import models.entities.RentStatus.RentStatus
import models.entities.Role.Role
import models.entities.{AccountType, RentStatus, Role}
import slick.driver.PostgresDriver.api._

object MappedColumnTypes {
  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )
  implicit val rentStatusColumnType = MappedColumnType.base[RentStatus, String]( { rs => rs.toString }, { s => RentStatus.withName(s) } )
  implicit val accountTypeColumnType = MappedColumnType.base[AccountType, String]( { rs => rs.toString }, { s => AccountType.withName(s) } )
}
