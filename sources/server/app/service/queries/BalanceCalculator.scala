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
    withDb { implicit session =>
      RepairTable.filter(r => r.rentId === rent.id && (r.repairDate <= asOfDate.get || asOfDate.isEmpty))
        .map(r => r.cost).foldLeft(BigDecimal("0"))(_ + _)
    }
  }
  override def getFinesTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    withDb { implicit session =>
      FineTable.filter(f => f.rentId === rent.id && (f.fineDate <= asOfDate.get || asOfDate.isEmpty))
        .map(f => f.cost).foldLeft(BigDecimal("0"))(_ + _)
    }
  }
  override def getPaymentsTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    withDb { implicit session =>
      PaymentTable.filter(p => p.rentId === rent.id && (p.payDate <= asOfDate.get || asOfDate.isEmpty))
        .map(p => p.amount).foldLeft(BigDecimal("0"))(_ + _)
    }
  }

  def getDebtsTotal(rent: Rent, asOfDate: Option[Timestamp]): BigDecimal = {
    val car = withDb { implicit session =>
      CarTable.filter(c => c.id === rent.carId).run.head
    }
    val rate = car.rate
    val periods = getStatusPeriods(rent, asOfDate).filter(p => p._1.status == RS.Active)
    periods.map(_._2).foldLeft(BigDecimal("0")){(seed, i) => seed + i*rate }
  }

  override def getDriverBalance(driver: Tables.Driver, asOfDate: Option[Timestamp]): BigDecimal = 0

  private def getStatusPeriods(rent: Rent, asOfDate: Option[Timestamp]): Seq[(RentStatus, Long)] = {
    val statuses = withDb { implicit session =>
      val statusQuery = RentStatusTable.filter(s => s.rentId === rent.id)
      asOfDate match {
        case Some(date) => statusQuery.filter(s => s.changeDate <= date).run
        case None => statusQuery.run
      }
    }.sortBy { s => s.changeDate.getTime }.toList
    statuses.indices.map { i =>
      val current = statuses(i)
      val currentDate = current.changeDate
      val nextDate = if (i + 1 <= statuses.size - 1){
        statuses(i + 1).changeDate
      } else {
        if (current.status == RS.Closed) current.changeDate
        else asOfDate getOrElse DateUtils.now
      }
      (current, getDateDiff(currentDate, nextDate))
    }
  }

  private def getDateDiff(date1: Timestamp, date2: Timestamp) = {
    val diffInMilliseconds = date2.getTime - date1.getTime
    DAYS.convert(diffInMilliseconds, MILLISECONDS)
  }
}
