package unit.services

import java.sql.Timestamp

import base.SpecificationWithFixtures
import helpers.DomainDSL
import models.entities.RentStatus
import models.generated.Tables.Rent
import service.queries.BalanceCalculator

class BalanceCalculatorSpec extends SpecificationWithFixtures {

  val date = """(\d\d\d\d)-(\d\d)-(\d\d)""".r
  implicit def StringToTimestamp(value: String): Timestamp = value match {
    case date(_*) => Timestamp.valueOf(value + " 00:00:00")
    case _ => Timestamp.valueOf(value)
  }

  "should calculate total balance for closed rent" in new WithFakeDB() {
    // given:
    val rent = createRent(inject [DomainDSL], closed = true)
    val calc = inject [BalanceCalculator]
    val date: Option[Timestamp] = Some("2015-03-30")

    // when:
    val repairs = calc.getRepairsTotal(rent, date)
    val fines = calc.getFinesTotal(rent, date)
    val payments = calc.getPaymentsTotal(rent, date)
    val debts = calc.getDebtsTotal(rent, date)
    val balance = calc.getRentBalance(rent, date)

    // then:
    repairs must beEqualTo(40)
    fines must beEqualTo(20)
    payments must beEqualTo(180)
    debts must beCloseTo(BigDecimal(132.17), BigDecimal(0.01))
    balance must beCloseTo(50 + 180 - 40 - 10 - BigDecimal(132.17), BigDecimal(0.01))
  }

  private def createRent(domain: DomainDSL, closed: Boolean = true): Rent = {
    implicit val rent: Rent = domain.rent(domain.driver(), domain.car(10), 50, "2015-03-01 14:50:22")

    domain.rentStatus(RentStatus.Active,          "2015-03-01 14:50:23")
    domain.payment(20,                            "2015-03-02 09:13:22", "2015-03-02 09:15:00")
    domain.payment(15,                            "2015-03-04 10:45:22", "2015-03-04 10:45:00")
    domain.fine(        10,                       "2015-03-05 12:33:34", "2015-03-03 16:00:00")
    domain.payment(40,                            "2015-03-07 08:12:45", "2015-03-07 08:15:00")

    domain.rentStatus(RentStatus.Suspended,       "2015-03-07 19:46:23")
    domain.fine(        5,                        "2015-03-09 09:34:56", "2015-03-06 17:30:00")

    domain.rentStatus(RentStatus.Active,          "2015-03-10 08:10:34")
    domain.payment(10,                            "2015-03-10 08:15:33", "2015-03-10 08:15:00")
    domain.repair(            15,                 "2015-03-12 16:34:23", "2015-03-12 10:35:00")
    domain.payment(25,                            "2015-03-13 08:33:23", "2015-03-13 08:30:00")

    domain.rentStatus(RentStatus.Suspended,       "2015-03-13 20:34:22")
    domain.payment(10,                            "2015-03-14 13:33:23", "2015-03-13 18:00:00")

    domain.rentStatus(RentStatus.Active,          "2015-03-15 08:20:34")
    domain.payment(10,                            "2015-03-15 08:31:45", "2015-03-15 08:30:00")
    domain.repair(            25,                 "2015-03-17 16:03:56", "2015-03-17 15:00:00")
    domain.payment(40,                            "2015-03-18 08:30:44", "2015-03-18 08:30:00")

    if (closed){
      domain.rentStatus(RentStatus.SettlingUp,    "2015-03-18 20:14:33")
      domain.fine(      5,                        "2015-03-19 15:55:23", "2015-03-07 17:30:00")
      domain.payment(10,                          "2015-03-20 12:56:22", "2015-03-20 12:56:22")

      domain.rentStatus(RentStatus.Closed,        "2015-03-25 08:12:44")
    }
    rent
  }
}

