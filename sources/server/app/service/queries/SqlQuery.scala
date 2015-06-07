package service.queries

import java.nio.charset.Charset

import play.api.Logger
import repository.db.DbAccessor
import utils.RunSqlException

import scala.slick.jdbc.{StaticQuery => Q, GetResult}

abstract class SqlQuery extends QueryImpl  with DbAccessor {
  protected def readSql(parameters: Map[String, String]): String = {
    val decoder = Charset.forName("UTF-8").newDecoder()
    val source = scala.io.Source.fromFile(s"conf/sql/$name.sql")(decoder)
    val sql = try source.getLines() mkString "\n" finally source.close()
    if (parameters.isEmpty) sql
    else parameters.foldLeft(sql)((agg, param) => agg.replaceAll(param._1, param._2))
  }

  protected def fetchResult[T](sql: String)(implicit getResult: GetResult[T]): List[T] = {
    val query = Q.queryNA[T](sql)
    try {
      withDb { implicit session => query.list }
    }
    catch {
      case e: Throwable =>
        Logger.error(s"Ошибка при выполнении запроса $name", e)
        throw new RunSqlException(s"Ошибка получения данных ($name)", e)
    }
  }
}
