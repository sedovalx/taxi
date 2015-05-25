package unit.services

import java.sql.Timestamp

import base.SpecificationWithFixtures
import helpers.DomainHelper
import models.entities.RentStatus
import models.generated.Tables.{Payment, Driver, Fine, Repair}
import service.queries.BalanceCalculator

import scala.language.implicitConversions

class BalanceCalculatorSpec extends SpecificationWithFixtures {
  "should calculate closed rent balance as of future date" in new WithFakeDB() {
    // setup:
    var rent = createRent(1000, 7500)(
      // rent=8*1000, payments=3600, repairs=1700, fines=500, balance=3600-8000-1700-500=-6600
      ActiveState("2015-03-01")(
        Payment("2015-03-03", 1200),
        Payment("2015-03-05", 900),
        Repair("2015-03-05", 1700),
        Payment("2015-03-08", 1500),
        Fine("2015-03-09", 500)
      ),
      // rent=0, payments=1300, fines=1000, balance=1300-1000=300
      SuspendedState("2015-03-09")(
        Payment("2015-03-10", 1300),
        Fine("2015-03-13", 1000)
      ),
      // rent=8*1000, payments=7000, repairs=5000, balance=7000-8000-5000=-6000
      ActiveState("2015-03-14")(
        Payment("2015-03-14", 2000),
        Repair("2015-03-15", 5000),
        Payment("2015-03-17", 3000),
        Payment("2015-03-21", 2000)
      ),
      // rent=0, payments=5000, fines=500, balance=5000-500=4500
      SettlingUpState("2015-03-22")(
        Fine("2015-03-25", 500),
        Payment("2015-03-26", 5000)
      ),
      // balance=7500-6600+300-6000+4500=-300
      ClosedState("2015-03-28")()
    )

    // when:
    val date = Some(Timestamp.valueOf("2015-04-02"))
    val calculator = inject [BalanceCalculator]
    val repairBalance = calculator.getRepairsTotal(rent, date)
    val fineBalance = calculator.getFinesTotal(rent, date)
    val paymentBalance = calculator.getPaymentsTotal(rent, date)
    val rentBalance = calculator.getBalance(rent, date)
    
    // then:
    rentBalance must beEqualTo(-300)
    repairBalance must beEqualTo(6700)
    fineBalance must beEqualTo(2000)
    paymentBalance must beEqualTo(16900)
  }

  "should calculate active rent balance as of future date" in new WithFakeDB() {
    // setup:
    val rent = createRent(100, 500)(
      // 500
      ActiveState("2015-03-01")(
        Payment("2015-03-03", 200),
        Repair("2015-03-04", 100),
        Fine("2015-03-05", 50)
      ),
      // 0
      SuspendedState("2015-03-06")(
        Payment("2015-03-06", 300),
        Fine("2015-03-10", 100)
      ),
      // 500
      ActiveState("2015-03-15")(
        Payment("2015-03-16", 200),
        Repair("2015-03-17", 200)
      )
    )

    // when:
    val date = Some(Timestamp.valueOf("2015-03-19"))
    val calculator = inject [BalanceCalculator]
    val repairBalance = calculator.getRepairsTotal(rent, date)
    val fineBalance = calculator.getFinesTotal(rent, date)
    val paymentBalance = calculator.getPaymentsTotal(rent, date)
    val rentBalance = calculator.getBalance(rent, date)

    // then:
    repairBalance must beEqualTo(300)
    fineBalance must beEqualTo(150)
    paymentBalance must beEqualTo(800)
    rentBalance must beEqualTo(500 + 800 - 1000 - 300 - 150)
  }

  "should calculate rent balance as of past date" in new WithFakeDB() {
    // setup:
    val rent = createRent(100, 500)(
      // 500
      ActiveState("2015-03-01")(
        Payment("2015-03-03", 200),
        Repair("2015-03-04", 100),
        Fine("2015-03-05", 50)
      ),
      // 0
      SuspendedState("2015-03-06")(
        Payment("2015-03-06", 300),
        Fine("2015-03-10", 100)
      ),
      // 500
      ActiveState("2015-03-15")(
        Payment("2015-03-16", 200),
        Repair("2015-03-17", 200)
      )
    )

    // when:
    val date = Some(Timestamp.valueOf("2015-03-14"))
    val calculator = inject [BalanceCalculator]
    val repairBalance = calculator.getRepairsTotal(rent, date)
    val fineBalance = calculator.getFinesTotal(rent, date)
    val paymentBalance = calculator.getPaymentsTotal(rent, date)
    val rentBalance = calculator.getBalance(rent, date)

    // then:
    repairBalance must beEqualTo(100)
    fineBalance must beEqualTo(150)
    paymentBalance must beEqualTo(500)
    rentBalance must beEqualTo(500 + 500 - 500 - 100 - 150)
  }

  implicit def StringToTimestamp(value: String): Timestamp = Timestamp.valueOf(value)
  implicit def RentStateToRentStatus(value: RentState): RentStatus.RentStatus = value match {
    case _: ActiveState => RentStatus.Active
    case _: SuspendedState => RentStatus.Suspended
    case _: SettlingUpState => RentStatus.SettlingUp
    case _: ClosedState => RentStatus.Closed
  }

  trait BalanceChange {
    val date: Timestamp
    val amount: BigDecimal
  }

  case class Payment(date: String, amount: BigDecimal) extends BalanceChange
  case class Fine(date: String, amount: BigDecimal) extends BalanceChange
  case class Repair(date: String, amount: BigDecimal) extends BalanceChange

  trait RentState {
    val date: Timestamp
    val changes: Seq[BalanceChange]
  }
  case class ActiveState(date: String)(val changes: BalanceChange*) extends RentState
  case class SuspendedState(date: String)(val changes: BalanceChange*) extends RentState
  case class ClosedState(date: String)(val changes: BalanceChange*) extends RentState
  case class SettlingUpState(date: String)(val changes: BalanceChange*) extends RentState

  def createRent(rate: Int, deposit: BigDecimal)(statuses: RentState*) = {
    val domainHelper = inject [DomainHelper]
    import domainHelper._
    val driver = createDriver()
    val car = createCar(rate)
    val s = statuses.map ( i => i.date -> RentStateToRentStatus(i) ).toMap
    val rent = createRent(driver, car, deposit, s)
    statuses flatMap { s => s.changes } foreach {
      case Payment(date, amount) =>
        createPayment(rent, amount, date)
      case Fine(date, amount) =>
        createFine(rent, amount, date)
      case Repair(date, amount) =>
        createRepair(rent, amount, date)
    }
    rent
  }
}
