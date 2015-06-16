package service.query

import play.api.Logger
import play.api.libs.json.JsValue

import scala.concurrent.Future

trait Query {
  val name: String
  def execute(parameters: Map[String, Seq[String]]): Future[JsValue]
}

abstract class QueryImpl extends Query {
  protected def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue]

  override def execute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    Logger.info(s"Execution of $name query with params: $parameters")
    doExecute(parameters)
  }
}






