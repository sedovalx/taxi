package service.queries

import models.generated.Tables.PaymentFilter
import play.api.libs.json.{JsValue, _}
import repository.PaymentRepo
import scaldi.{Injectable, Injector}
import serialization.PaymentSerializer
import scala.util.Try

class PaymentsQuery(implicit injector: Injector) extends CodeQuery with Injectable {
  override val name: String = "q-payment-list"
  private val serializer = inject [PaymentSerializer]
  import serializer._

  override def doExecute(parameters: Map[String, Seq[String]]): JsValue = {
    val filter = parseFilter(parameters)

    val repo = inject[PaymentRepo]
    val data = withDb { implicit session => repo.read(Some(filter)) }
    JsObject(Seq("payments" -> Json.toJson(data)))
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): PaymentFilter = {
    parameters.get("rent")
      .flatMap { values => values.headOption }
      .flatMap { s => Try(s.toInt).toOption } match {
      case Some(rent) => PaymentFilter(rentId = Some(rent))
      case _ => PaymentFilter()
    }
  }
}
