package query

import javax.inject.Inject

import models.entities.AccountType
import models.generated.Tables.OperationFilter
import play.api.libs.json.{JsValue, _}
import repository.OperationRepo
import serialization.entity.OperationSerializer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class OperationQuery @Inject() (serializer: OperationSerializer, repo: OperationRepo) extends CodeQuery {
  import serializer._

  override val name: String = "q-operation-list"

  override def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    val filter = parseFilter(parameters)

    repo.read(Some(filter)) map { items => JsObject(Seq("operations" -> Json.toJson(items))) }
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
