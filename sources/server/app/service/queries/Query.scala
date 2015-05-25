package service.queries

import play.api.libs.json.JsArray

trait Query {
  val name: String
  def execute(parameters: Map[String, Seq[String]]): JsArray
}

abstract class CodeQuery extends Query

abstract class XmlQuery extends Query




