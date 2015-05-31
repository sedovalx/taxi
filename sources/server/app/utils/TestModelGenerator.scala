package utils

import java.sql.Timestamp
import java.util.UUID

import models.generated.Tables._
import models.entities.{RentStatus => RS}
import repository._
import repository.db.DbAccessor
import scaldi.{Injectable, Injector}
import utils.extensions.DateUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import play.api.db.slick.Config.driver.simple._

class TestModelGenerator(implicit val injector: Injector) extends Injectable with DbAccessor{
  def generate() = Future {
    withDb { implicit session => session.withTransaction {
      deleteAll
      generateDrivers(20, Timestamp.valueOf("2015-04-28 00:00:00"))
      generateCars(50, Timestamp.valueOf("2015-04-03 00:00:00"))
      generateRents(18, 44, Timestamp.valueOf("2015-05-15 00:00:00"))
      generateRentStates(60)
      generateBalanceChanges
    }}
  }

  def deleteAll(implicit session: Session): Unit = {
    PaymentTable.delete.run
    FineTable.delete.run
    RepairTable.delete.run
    RentStatusTable.delete.run
    RentTable.delete.run
    DriverTable.delete.run
    CarTable.delete.run
  }

  def generateBalanceChanges(implicit session: Session): Unit = {
    import repository.db.MappedColumnTypes._
    val rents = RentStatusTable.groupBy(s => s.rentId).map { case (rentId, group) =>
      val startTime = group.map(s => s.changeTime).min
      val endTime = group.map(s => s.changeTime).max
      val isClosed = group.map { s => Case If(s.status === RS.Closed) Then 1 Else 0 }.sum > 0
      (rentId, startTime, endTime, isClosed)
    }.run

    rents.map { i =>
      (
        i._1,
        i._2.get,
        i._3.get,
        i._4 match { case Some(x) if x => true case _ => false }
      )
    }.foreach { r =>
      val changesCount = Random.nextInt(20) + 2
      (0 to changesCount).foreach { _ =>
        Random.nextInt(5) match {
          case 0 | 1 | 2 =>
            createPayment(r._1, r._2, if (r._4) r._3 else DateUtils.now)
          case 3 =>
            createFine(r._1, r._2, if (r._4) r._3 else DateUtils.now)
          case 4 =>
            createRepair(r._1, r._2, if (r._4) r._3 else DateUtils.now)
        }
      }
    }
  }

  private def createPayment(rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    val creationTime = getRandomDateBetween(startTime, endTime)
    val repo = inject [PaymentRepo]
    repo.create(Payment(
      id = 0,
      rentId = rentId,
      changeTime = creationTime,
      amount = BigDecimal(Random.nextDouble()*4000 + 1000),
      creationDate = Some(creationTime)
    ))
  }

  private def createFine(rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    val creationTime = getRandomDateBetween(startTime, endTime)
    val repo = inject [FineRepo]
    repo.create(Fine(
      id = 0,
      rentId = rentId,
      changeTime = creationTime,
      amount = BigDecimal(Random.nextDouble()*500 + 100),
      creationDate = Some(creationTime)
    ))
  }

  private def createRepair(rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    val creationTime = getRandomDateBetween(startTime, endTime)
    val repo = inject [RepairRepo]
    repo.create(Repair(
      id = 0,
      rentId = rentId,
      changeTime = creationTime,
      amount = BigDecimal(Random.nextDouble()*1500 + 500),
      creationDate = Some(creationTime)
    ))
  }

  def generateRentStates(closedPercent: Int)(implicit session: Session): Unit = {
    RentTable.map(r => (r.id, r.creationDate)).run.foreach { i =>
      addGeneralStates(i._1, i._2.get)
      if (isClosed(closedPercent)){
        addFinalStates(i._1, i._2.get)
      }
    }
  }

  private def addGeneralStates(rentId: Int, startTime: Timestamp)(implicit session: Session) = {
    val statesCount = Random.nextInt(5) + 1
    var times = List[Timestamp]()
    (0 to statesCount).foldLeft(startTime){ (t, n) =>
      times :+= t
      val endTime = getDateBetween(times(n), DateUtils.now, 0.5f)
      getRandomDateBetween(times(n), endTime)
    }
    val statuses = times.indices.map { i =>
      RentStatus(
        id = 0,
        changeTime = times(i),
        status = if (i % 2 == 0) RS.Active else RS.Suspended,
        rentId = rentId,
        creationDate = Some(times(i)))
    }

    val repo = inject [RentStatusRepo]
    statuses.foreach { s => repo.create(s) }
  }

  private def addFinalStates(rentId: Int, startTime: Timestamp)(implicit session: Session) = {
    val lastStatus = RentStatusTable.filter { s => s.rentId === rentId }.sortBy(s => s.changeTime.desc).run.head
    val startDate = lastStatus.changeTime
    val settlingTime = getRandomDateBetween(startDate, DateUtils.now)
    val closedTime = getRandomDateBetween(settlingTime, DateUtils.now)
    val repo = inject [RentStatusRepo]
    repo.create(RentStatus(
      id = 0,
      rentId = rentId,
      changeTime = settlingTime,
      status = RS.SettlingUp,
      creationDate = Some(settlingTime)
    ))
    repo.create(RentStatus(
      id = 0,
      rentId = rentId,
      changeTime = closedTime,
      status = RS.Closed,
      creationDate = Some(closedTime)
    ))
  }

  private def isClosed(closedPercent: Int): Boolean = {
    Random.nextInt(100) <= closedPercent
  }

  def generateRents(drivers: Int, cars: Int, endDate: Timestamp)(implicit session: Session): Unit = {
    val rentRepo = inject [RentRepo]
    val data = (for {
      car <- CarTable.sortBy(c => c.id*Random.nextInt(100)).take(cars)
      driver <- DriverTable.sortBy(d => d.id*Random.nextInt(100)).take(drivers)
    } yield (driver.id, car.id, driver.creationDate, car.creationDate)).run

    data.foreach { d =>
      val startDate = getMaxDate(d._3, d._4)
      val date = getRandomDateBetween(startDate, endDate)
      val rent = Rent(
        id = 0,
        driverId = d._1,
        carId = d._2,
        deposit = Random.nextInt(5000) + 1000,
        creationDate = Some(date)
      )
      rentRepo.create(rent)
    }
  }

  private def getMaxDate(dates: Option[Timestamp]*): Timestamp = {
    val t = dates.map { d => d.get.getTime }.max
    new Timestamp(t)
  }

  private def getDateBetween(startTime: Timestamp, endTime: Timestamp, k: Float) = {
    val start = startTime.getTime
    val end = endTime.getTime

    new Timestamp(start + (k*(end - start)).toLong)
  }

  private def getRandomDateBetween(startTime: Timestamp, endTime: Timestamp) =
    getDateBetween(startTime, endTime, Random.nextFloat())

  def generateDrivers(count: Int, onDate: Timestamp)(implicit session: Session): Unit = {
    val repo = inject [DriverRepo]
    (0 to count).foreach { _ =>
      val driver = Driver(
        id = 0,
        lastName = UUID.randomUUID().toString.take(200),
        firstName = UUID.randomUUID().toString.take(200),
        pass = UUID.randomUUID().toString,
        license = UUID.randomUUID().toString,
        phone = UUID.randomUUID().toString.take(11),
        secPhone = UUID.randomUUID().toString.take(11),
        address = UUID.randomUUID().toString,
        creationDate = Some(onDate)
      )
      repo.create(driver)
    }
  }

  def generateCars(count: Int, onDate: Timestamp)(implicit session: Session): Unit = {
    val repo = inject [CarRepo]
    (0 to count).foreach { _ =>
      val car = Car(
        id = 0,
        regNumber = UUID.randomUUID().toString.take(11),
        make = UUID.randomUUID().toString,
        model = UUID.randomUUID().toString,
        rate = Random.nextInt(5000) + 1000,
        mileage = 1000,
        creationDate = Some(onDate)
      )
      repo.create(car)
    }
  }
}
