package query

import java.io.FileNotFoundException
import java.nio.charset.Charset

import service.query.QueryImpl
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.{GetResult, StaticQuery => Q}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class SqlQuery(dbConfig: DatabaseConfig[JdbcProfile]) extends QueryImpl {
  protected def readSql(parameters: Map[String, String]): String = {
    val decoder = Charset.forName("UTF-8").newDecoder()
    val resourceStream = getClass.getResourceAsStream(s"/sql/$name.sql")
    if (resourceStream == null)
      throw new FileNotFoundException(s"/sql/$name.sql")
    val source = scala.io.Source.fromInputStream(resourceStream)(decoder)
    val sql = try source.getLines() mkString "\n" finally source.close()
    if (parameters.isEmpty) sql
    else parameters.foldLeft(sql)((agg, param) => agg.replaceAll(param._1, param._2))
  }

  protected def fetchResult[T](sql: String)(implicit getResult: GetResult[T]): Future[Seq[T]] = Future {
    dbConfig.db.withSession { implicit session =>
      Q.queryNA[T](sql).list
    }
  }
}
