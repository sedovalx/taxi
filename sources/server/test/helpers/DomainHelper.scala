package helpers

import java.sql.Timestamp
import java.util.UUID

import repository._
import repository.db.DbAccessor
import scaldi.{Injectable, Injector}
import models.generated.Tables._
import models.entities.RentStatus.{RentStatus => RS}

class DomainHelper(implicit injector: Injector) extends Injectable with DbAccessor{
  def createDriver(): Driver = {
    val driverRepo = inject [DriverRepo]
    val driver = Driver(
      id = 0,
      lastName = "sad",
      firstName = "zx;lck",
      pass = UUID.randomUUID().toString,
      license = UUID.randomUUID().toString,
      phone = "12312",
      secPhone = "2342345",
      address = "sdsdf90")

    val driverId = withDb { session => driverRepo.create(driver)(session) }
    driver.copy(id = driverId)
  }

  def createCar(rate: BigDecimal): Car = {
    val carRepo = inject [CarRepo]
    val car = Car(id = 0, regNumber = UUID.randomUUID().toString.substring(0, 11), make = "asd", model = "bfgh", rate = rate, mileage = 1000)
    val carId = withDb { session => carRepo.create(car)(session) }
    car.copy(id = carId)
  }

  def createRent(driver: Driver, car: Car, deposit: BigDecimal, statuses: Map[Timestamp, RS]): Rent = {
    val rentRepo = inject [RentRepo]
    val statusRepo = inject [RentStatusRepo]
    val rent = Rent(id = 0, driverId = driver.id, carId = car.id, deposit = deposit)
    val rentId = withDb { session => rentRepo.create(rent)(session) }
    val saved = rent.copy(id = rentId)
    withDb { session =>
      statuses.foreach { item =>
        val date = item._1
        val status = item._2
        val rentStatus = RentStatus(id = 0, changeDate = date, status = status, rentId = saved.id)
        statusRepo.create(rentStatus)(session)
      }
    }
    saved
  }

  def createRepair(rent: Rent, amount: BigDecimal, date: Timestamp): Repair = {
    val repo = inject [RepairRepo]
    val repair = Repair(id = 0, repairDate = date, cost = amount, rentId = rent.id)
    val repairId = withDb { implicit session => repo.create(repair) }
    repair.copy(id = repairId)
  }

  def createFine(rent: Rent, amount: BigDecimal, date: Timestamp): Fine = {
    val repo = inject [FineRepo]
    val fine = Fine(id = 0, fineDate = date, cost = amount, rentId = rent.id)
    val fineId = withDb { implicit session => repo.create(fine) }
    fine.copy(id = fineId)
  }

  def createPayment(rent: Rent, amount: BigDecimal, date: Timestamp): Payment = {
    val repo = inject [PaymentRepo]
    val payment = Payment(id = 0, payDate = date, amount = amount, rentId = rent.id)
    val paymentId = withDb { implicit session => repo.create(payment) }
    payment.copy(id = paymentId)
  }
}
