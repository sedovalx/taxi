package service.queries

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Rent, Driver}

trait BalanceCalculator {
  def getBalance(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getRepairsTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getFinesTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal
  def getPaymentsTotal(rent: Rent, asOfDate: Option[Timestamp] = None): BigDecimal

  def getBalance(driver: Driver, asOfDate: Option[Timestamp]): BigDecimal
}

class BalanceCalculatorImpl extends BalanceCalculator {
  override def getBalance(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = 0
  override def getRepairsTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = 0
  override def getFinesTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = 0
  override def getPaymentsTotal(rent: Tables.Rent, asOfDate: Option[Timestamp]): BigDecimal = 0

  override def getBalance(driver: Tables.Driver, asOfDate: Option[Timestamp]): BigDecimal = 0
}
