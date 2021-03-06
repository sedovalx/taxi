package utils.slick

import play.api.Logger
import slick.driver.JdbcProfile

import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait SourceCodeGenerator {
  def run(slickDriver: String, jdbcDriver: String, url: String, outputDir: String, pkg: String, user: String, password: String): Unit
}

class SourceCodeGeneratorImpl extends SourceCodeGenerator {
  def run(slickDriver: String, jdbcDriver: String, url: String, outputDir: String, pkg: String, user: String, password: String): Unit = {
    val driver: JdbcProfile =
      Class.forName(slickDriver + "$").getField("MODULE$").get(null).asInstanceOf[JdbcProfile]
    val dbFactory = driver.api.Database
    val db = dbFactory.forURL(url, driver = jdbcDriver, user = user, password = password, keepAliveConnection = true)
    val tables = driver.defaultTables map { tables =>
      tables filter { t =>
        t.name.name != "play_evolutions"
      }
    }
    db.run(driver.createModel(Some(tables), ignoreInvalidDefaults = false)(ExecutionContext.global).withPinnedSession)
      .map { model => new AppSourceCodeGenerator(model) }
      .onComplete {
      case Success(generator) => generator.writeToFile(slickDriver,outputDir,pkg)
      case Failure(t) => Logger.error("Slick code generation error", t)
    }
  }
}




