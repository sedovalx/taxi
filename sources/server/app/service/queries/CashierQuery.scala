package service.queries

import java.sql.Timestamp

import models.entities.RentStatus.RentStatus
import play.api.libs.json.JsArray
import scaldi.{Injector, Injectable}
//import models.generated.Tables._
//import play.api.db.slick.Config.driver.simple._
//import scala.slick.jdbc.StaticQuery.interpolation
//import scala.slick.jdbc.GetResult


class CashierQuery(implicit injector: Injector) extends CodeQuery with Injectable {
  override val name: String = "q-cashier"

  case class Filter(car: Option[String] = None, driver: Option[String] = None, status: Option[RentStatus] = None, date: Option[Timestamp] = None)

  override def execute(parameters: Map[String, Seq[String]]): JsArray = {
    ???
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): Filter = Filter()
}

