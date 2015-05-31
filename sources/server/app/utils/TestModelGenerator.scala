package utils

import java.sql.Timestamp
import java.util.UUID

import models.generated.Tables._
import models.entities.{RentStatus => RS}
import repository.{RentStatusRepo, RentRepo, CarRepo, DriverRepo}
import repository.db.DbAccessor
import scaldi.{Injectable, Injector}
import utils.extensions.DateUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import play.api.db.slick.Config.driver.simple._

class TestModelGenerator(implicit val injector: Injector) extends Injectable with DbAccessor{
  def generate() = Future {
    generateDrivers(20, Timestamp.valueOf("2015-04-28 00:00:00"))
    generateCars(50, Timestamp.valueOf("2015-04-03 00:00:00"))
    generateRents(18, 44, Timestamp.valueOf("2015-05-15"))
  }

  def generateRentStates(closedPercent: Int) = {
    withDb { implicit session =>
      RentTable.map(r => (r.id, r.creationDate)).run.foreach { i =>
        addGeneralStates(i._1, i._2.get)
        if (isClosed(closedPercent)){
          addFinalStates(i._1, i._2.get)
        }
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

  def generateRents(drivers: Int, cars: Int, endDate: Timestamp) = {
    val rentRepo = inject [RentRepo]
    withDb { implicit session =>
      val data = (for {
        car <- CarTable.sortBy(c => c.id*Random.nextInt()).take(cars)
        driver <- DriverTable.sortBy(d => d.id*Random.nextInt()).take(drivers)
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
  }

  private def getMaxDate(dates: Option[Timestamp]*): Timestamp = {
    dates.map { case Some(d) => d }.max
  }

  private def getDateBetween(startTime: Timestamp, endTime: Timestamp, k: Float) = {
    val start = startTime.getTime
    val end = endTime.getTime

    new Timestamp(start + (k*(end - start)).toLong)
  }

  private def getRandomDateBetween(startTime: Timestamp, endTime: Timestamp) =
    getDateBetween(startTime, endTime, Random.nextFloat())

  def generateDrivers(count: Int, onDate: Timestamp) = {
    val repo = inject [DriverRepo]
    withDb { implicit session =>
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
  }

  def generateCars(count: Int, onDate: Timestamp) = {
    val repo = inject [CarRepo]
    withDb { implicit session =>
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
}
