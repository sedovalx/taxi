package service.queries

import java.nio.charset.Charset

import play.api.libs.json.JsValue

trait Query {
  val name: String
  def execute(parameters: Map[String, Seq[String]]): JsValue
}

abstract class ConfQuery extends Query {
  protected def readSql(parameters: Map[String, String]): String = {
    val decoder = Charset.forName("UTF-8").newDecoder()
    val source = scala.io.Source.fromFile(s"conf/sql/$name.sql")(decoder)
    val sql = try source.getLines() mkString "\n" finally source.close()
    if (parameters.isEmpty) sql
    else parameters.foldLeft(sql)((agg, param) => agg.replaceAll(param._1, param._2))
  }
}




