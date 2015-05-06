package controllers

import base.BaseControllerSpecification
import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables.{Car, Driver, Rent, RentStatus => RS}
import play.api.Application
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.test.Helpers._
import repository.{CarRepo, DriverRepo, RentRepo, RentStatusRepo}
import utils.extensions.DateUtils

class RentControllerTest extends BaseControllerSpecification with org.specs2.matcher.JsonMatchers {
  "should create status history record on rent creation" in new WithFakeDB {
    // setup:
    val driverId = createDriver()
    val carId = createCar()
    val rentJson = Json.obj(
      "rent" -> Json.obj(
        "driver" -> driverId.toString,
        "car" -> carId.toString,
        "deposit" -> "3453.12",
        "status" -> RentStatus.Active.toString
      )
    )
    val createRequest = createEmptyAuthenticatedRequest(POST, "/api/rents").withJsonBody(rentJson)

    // test:
    val Some(createResponse) = route(createRequest)

    // check:
    status(createResponse) must beEqualTo(OK)
    val id = (contentAsJson(createResponse) \ "rent" \ "id").as[String]
    val statuses = getStatuses(id.toInt)
    statuses must have size 1
  }

  "should create status history record on rent status update" in new WithFakeDB {
    // setup:
    val rentId = createRent()
    createRentStatuses(rentId, RentStatus.Active, RentStatus.Suspended, RentStatus.Active, RentStatus.SettlingUp)
    val updateRequest = createEmptyAuthenticatedRequest(PUT, "/api/rents/" + rentId)
      .withJsonBody(Json.obj(
        "rent" -> Json.obj(
          "driver" -> "1",
          "car" -> "1",
          "deposit" -> "2000",
          "status" -> RentStatus.Closed.toString
        )
      ))

    // test:
    val Some(updateResponse) = route(updateRequest)

    // check:
    status(updateResponse) must beEqualTo(OK)
    val statuses = getStatuses(rentId)
    statuses must have size 5
    statuses.sorted(Ordering.by((i: RS) => i.changeDate.getTime).reverse).head.status must beEqualTo(RentStatus.Closed)
  }

  "should not create status history record on rent update if status the same" in new WithFakeDB {
    // setup:
    val rentId = createRent()
    createRentStatuses(rentId, RentStatus.Active, RentStatus.Suspended, RentStatus.Active, RentStatus.SettlingUp)
    val updateRequest = createEmptyAuthenticatedRequest(PUT, "/api/rents/" + rentId)
      .withJsonBody(Json.obj(
      "rent" -> Json.obj(
        "driver" -> "1",
        "car" -> "1",
        "deposit" -> "2000",
        "status" -> RentStatus.SettlingUp.toString
      )
    ))

    // test:
    val Some(updateResponse) = route(updateRequest)

    // check:
    status(updateResponse) must beEqualTo(OK)
    val statuses = getStatuses(rentId)
    statuses must have size 4
  }

  "should return rent JSON with actual status" in new WithFakeDB {
    // setup:
    val rentId = createRent()
    createRentStatuses(rentId, RentStatus.Active, RentStatus.Suspended, RentStatus.Active, RentStatus.SettlingUp)
    val readRequest = createEmptyAuthenticatedRequest(GET, "/api/rents")

    // test:
    val Some(readResponse) = route(readRequest)

    // check:
    status(readResponse) must beEqualTo(OK)
    contentAsString(readResponse) must /("rents") /# 0 /("status" -> "SettlingUp")
  }

  private def createRentStatuses(rentId: Int, statuses: RentStatus*)(implicit app: Application) = {
    implicit val injector = global.injector
    val rentStatusRepo = inject [RentStatusRepo]
    DB.withSession { session =>
      statuses.foreach { s =>
        val rentStatus = RS(id = 0, changeDate = DateUtils.now, status = s, rentId = rentId)
        rentStatusRepo.create(rentStatus)(session)
      }
    }
  }

  private def createRent()(implicit app: Application): Int = {
    implicit val injector = global.injector
    val rentRepo = inject [RentRepo]
    val rent = Rent(id = 0, driverId = createDriver(), carId = createCar(), deposit = 1000)
    DB.withSession { session =>
      rentRepo.create(rent)(session)
    }
  }

  private def createDriver()(implicit app: Application): Int = {
    implicit val injector = global.injector
    val driverRepo = inject [DriverRepo]
    val driver = Driver(id = 0, pass = "asd", license = "asdas", phone = "12312", secPhone = "2342345", address = "sdsdf90")
    DB.withSession { session =>
      driverRepo.create(driver)(session)
    }
  }

  private def createCar()(implicit app: Application): Int = {
    implicit val injector = global.injector
    val carRepo = inject [CarRepo]
    val car = Car(id = 0, regNumber = "123123", make = "asd", model = "bfgh", rate = 100, mileage = 1000)
    DB.withSession { session =>
      carRepo.create(car)(session)
    }
  }

  private def getStatuses(rentId: Int)(implicit app: Application) = {
    implicit val injector = global.injector
    val rentStatusRepo = inject [RentStatusRepo]
    DB.withSession { session =>
      rentStatusRepo.tableQuery.filter(_.rentId === rentId).run(session)
    }
  }
}
