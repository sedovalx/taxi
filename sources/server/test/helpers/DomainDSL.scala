package helpers

import java.sql.Timestamp
import java.util.UUID

import models.entities.AccountType
import models.entities.AccountType.AccountType
import models.entities.AccountType.AccountType
import models.entities.RentStatus.{RentStatus => RS}
import models.generated.Tables._
import repository._
import repository.db.DbAccessor
import scaldi.{Injector, Injectable}
import utils.extensions.DateUtils

import scala.language.implicitConversions

class DomainDSL(implicit injector: Injector) extends Injectable with DbAccessor {
  val date = """(\d\d\d\d)-(\d\d)-(\d\d)""".r
  implicit def StringToTimestamp(value: String): Timestamp = value match {
    case date(_*) => Timestamp.valueOf(value + " 00:00:00")
    case _ => Timestamp.valueOf(value)
  }

  def driver(): Driver = {
    val driverRepo = inject [DriverRepo]
    val driver = Driver(
      id = 0,
      lastName = UUID.randomUUID().toString.take(200),
      firstName = UUID.randomUUID().toString.take(200),
      pass = UUID.randomUUID().toString,
      license = UUID.randomUUID().toString,
      phone = UUID.randomUUID().toString.take(11),
      secPhone = UUID.randomUUID().toString.take(11),
      address = UUID.randomUUID().toString)

    val driverId = withDb { session => driverRepo.create(driver)(session) }
    driver.copy(id = driverId)
  }

  def car(rate: BigDecimal): Car = {
    val carRepo = inject [CarRepo]
    val car = Car(
      id = 0,
      regNumber = UUID.randomUUID().toString.take(11),
      make = UUID.randomUUID().toString,
      model = UUID.randomUUID().toString,
      rate = rate,
      mileage = 1000
    )
    val carId = withDb { session => carRepo.create(car)(session) }
    car.copy(id = carId)
  }

  def rent(driver: Driver, car: Car, deposit: BigDecimal, creationTime: String): Rent = {
    val rentRepo = inject [RentRepo]
    val rent = Rent(
      id = 0,
      driverId = driver.id,
      carId = car.id,
      deposit = deposit,
      creationDate = Some(creationTime)
    )
    val rentId = withDb { session => rentRepo.create(rent)(session) }
    rent.copy(id = rentId)
  }

  def rentStatus(status: RS, changeTime: String)(implicit rent: Rent) = {
    val repo = inject [RentStatusRepo]
    val obj = RentStatus(id = 0, changeTime = changeTime, status = status, rentId = rent.id, creationDate = Some(changeTime))
    val statusId = withDb { session => repo.create(obj)(session) }
    obj.copy(id = statusId)
  }

  def operation(accountType: AccountType, amount: BigDecimal, creationTime: String, changeTime: String)(implicit rent: Rent): Operation = {
    val repo = inject [OperationRepo]
    val change = Operation(
      id = 0,
      accountType = accountType,
      changeTime = changeTime,
      amount = amount,
      creationDate = Some(creationTime),
      rentId = rent.id
    )
    val changeId = withDb { session => repo.create(change)(session) }
    change.copy(id = changeId)
  }

  def operation(accountType: AccountType, amount: BigDecimal, creationTime: String)(implicit rent: Rent): Operation = {
    operation(accountType, amount, creationTime, creationTime)
  }

  def payment(amount: BigDecimal, creationTime: String, changeTime: String)(implicit rent: Rent): Operation = {
    operation(AccountType.Rent, amount, creationTime, changeTime)
  }

  def payment(amount: BigDecimal, creationTime: String)(implicit rent: Rent): Operation =
    payment(amount, creationTime, creationTime)(rent)

  def repair(amount: BigDecimal, creationTime: String, changeTime: String)(implicit rent: Rent): Operation = {
    operation(AccountType.Repair, amount, creationTime, changeTime)
  }

  def repair(amount: BigDecimal, creationTime: String)(implicit rent: Rent): Operation =
    repair(amount, creationTime, creationTime)(rent)

  def fine(amount: BigDecimal,creationTime: String, changeTime: String)(implicit rent: Rent): Operation = {
    operation(AccountType.Fine, amount, creationTime, changeTime)
  }

  def fine(amount: BigDecimal, creationTime: String)(implicit rent: Rent): Operation =
    fine(amount, creationTime, creationTime)(rent)
}

