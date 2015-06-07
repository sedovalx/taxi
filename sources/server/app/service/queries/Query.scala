package service.queries

import play.api.Logger
import play.api.libs.json.JsValue
import repository.db.DbAccessor

trait Query {
  val name: String
  def execute(parameters: Map[String, Seq[String]]): JsValue
}

abstract class QueryImpl extends Query with DbAccessor {

  protected def doExecute(parameters: Map[String, Seq[String]]): JsValue

  override def execute(parameters: Map[String, Seq[String]]): JsValue = {
    Logger.info(s"Execution of $name query with params: $parameters")
    doExecute(parameters)
  }
}






