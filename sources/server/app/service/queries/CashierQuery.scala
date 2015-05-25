package service.queries

import models.entities.RentStatus.RentStatus
import play.api.libs.json.JsArray
import scaldi.{Injector, Injectable}


class CashierQuery(implicit injector: Injector) extends CodeQuery with Injectable {
  override val name: String = "q-cashier"

  case class Filter(car: Option[String], driver: Option[String], status: Option[RentStatus])

  override def execute(parameters: Map[String, Seq[String]]): JsArray = {
    ???
  }
}

