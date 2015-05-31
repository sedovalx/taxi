package service.queries

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables._
import models.entities.{RentStatus => RS}
import play.api.db.slick.Config.driver.simple._
import repository.db.DbAccessor
import utils.extensions.DateUtils

import scala.concurrent.duration._

trait BalanceCalculator {
  def getRentBalance(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getRepairsTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getFinesTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getPaymentsTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getDebtsTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal

  def getDriverBalance(driver: Driver, asOfDate: Option[Timestamp] = None): BigDecimal
}

class BalanceCalculatorImpl extends BalanceCalculator with DbAccessor {
  override def getRentBalance(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    val deposit = rent.deposit
    val repairs = getRepairsTotal(rent, asOfDate)
    val fines = getFinesTotal(rent, asOfDate)
    val payments = getPaymentsTotal(rent, asOfDate)
    val debts = getDebtsTotal(rent, asOfDate)
    deposit + payments - debts - repairs - fines
  }
  override def getRepairsTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    val date = asOfDate getOrElse DateUtils.now
    withDb { implicit session =>
      RepairTable.filter(r => r.rentId === rent.id && wasBeforeDate(r.creationDate, r.changeTime, date))
        .map(r => r.amount).foldLeft(BigDecimal("0"))(_ + _)
    }
  }
  override def getFinesTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    val date = asOfDate getOrElse DateUtils.now
    withDb { implicit session =>
      FineTable.filter(f => f.rentId === rent.id && wasBeforeDate(f.creationDate, f.changeTime, date))
        .map(f => f.amount).foldLeft(BigDecimal("0"))(_ + _)
    }
  }
  override def getPaymentsTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    val date = asOfDate getOrElse DateUtils.now
    withDb { implicit session =>
      PaymentTable.filter(p => p.rentId === rent.id && wasBeforeDate(p.creationDate, p.changeTime, date))
        .map(p => p.amount).foldLeft(BigDecimal("0"))(_ + _)
    }
  }

  def getDebtsTotal(rent: Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    val date = asOfDate getOrElse DateUtils.now
    val car = withDb { implicit session =>
      CarTable.filter(c => c.id === rent.carId).run.head
    }
    val ratePerDay = car.rate
    val ratePerMin = ratePerDay / 24 / 60
    val periods = getStatusPeriods(rent, date).filter(p => p.period.status == RS.Active)
    periods.map { sp => getDateDiff(sp.start, sp.end, MINUTES) }.foldLeft(BigDecimal("0")){(seed, i) => seed + i*ratePerMin }
  }

  override def getDriverBalance(driver: Tables.Driver, asOfDate: Option[Timestamp]): BigDecimal = 0

  private def wasBeforeDate(creationTime: Column[Option[Timestamp]], changeTime: Column[Timestamp], asOfDate: Timestamp) = {
    (creationTime.isEmpty && changeTime <= asOfDate) || (creationTime <= asOfDate)
  }

  private case class StatusPeriod(period: RentStatus, start: Timestamp, end: Timestamp)

  private def getStatusPeriods(rent: Rent, limitDate: Timestamp): Seq[StatusPeriod] = {
    val statuses = withDb { implicit session =>
      RentStatusTable.filter(s => s.rentId === rent.id).filter(s => s.changeTime <= limitDate).sortBy { s => s.changeTime }.run.toList
    }
    statuses.indices.map { i =>
      val current = statuses(i)
      val currentDate = current.changeTime
      val nextDate = if (i + 1 <= statuses.size - 1){
        statuses(i + 1).changeTime
      } else {
        if (current.status == RS.Closed) current.changeTime
        else limitDate
      }
      StatusPeriod(current, currentDate, nextDate)
    }
  }

  private def getDateDiff(date1: Timestamp, date2: Timestamp, units: TimeUnit) = {
    val diffInMilliseconds = date2.getTime - date1.getTime
    units.convert(diffInMilliseconds, MILLISECONDS)
  }
}
