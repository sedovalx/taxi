package base

import config.ConfigurationLoader
import org.specs2.execute.{AsResult, Result}
import play.api.Application
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{PlaySpecification, WithApplication}
import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration._

class DatabaseSpecification extends PlaySpecification {
  protected def defaultAppBuilder =
    new GuiceApplicationBuilder()
      .configure(ConfigurationLoader.loadFirst("application.test.override.conf", "application.test.conf"))

//  protected def afterEach(app: Application) = {
//    recreateDbSchema(app)
//  }
//
//  private def recreateDbSchema(app: Application) = {
//    val dbConfig = DatabaseConfigProvider.get[JdbcProfile](app)
//    import dbConfig.driver.api._
//
//    val recreateSchema: DBIO[Unit] = DBIO.seq(
//      sqlu"drop schema public cascade",
//      sqlu"create schema public"
//    )
//
//    println("before db clean")
//    Await.ready(dbConfig.db.run(recreateSchema), 5 seconds)
//  }

  abstract class DatabaseContext() extends WithApplication(defaultAppBuilder.build()) {
    protected val injector = implicitApp.injector

    override def around[T](t: => T)(implicit evidence$2: AsResult[T]): Result = super.around {
      try {
        t
      } finally {
        //afterEach(implicitApp)
      }
    }
  }

}
