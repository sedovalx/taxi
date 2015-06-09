package service.queries

import models.entities.AccountType
import models.generated.Tables.OperationFilter
import play.api.libs.json.{JsValue, _}
import repository.OperationRepo
import scaldi.Injector
import serialization.OperationSerializer

import scala.util.Try

class OperationQuery(implicit injector: Injector) extends CodeQuery {
  override val name: String = "q-operation-list"
  private val serializer = inject [OperationSerializer]
  import serializer._

  override def doExecute(parameters: Map[String, Seq[String]]): JsValue = {
    val filter = parseFilter(parameters)

    val repo = inject[OperationRepo]
    val data = withDb { implicit session => repo.read(Some(filter)) }
    JsObject(Seq("payments" -> Json.toJson(data)))
  }

  private def parseFilter(parameters: Map[String, Seq[String]]): OperationFilter = {
    var filter = OperationFilter()
    parameters.get("rent")
      .flatMap { values => values.headOption }
      .flatMap { s => Try(s.toInt).toOption } match {
      case Some(rent) => filter = filter.copy(rentId = Some(rent))
      case _ =>
    }
    parameters.get("accountType")
      .flatMap { values => values.headOption }
      .flatMap { s => Try(AccountType.withName(s)).toOption } match {
      case Some(tpe) => filter = filter.copy(accountType = Some(tpe))
      case _ =>
    }
    filter
  }
}