package base

import com.google.common.collect.ImmutableMap
import config.ConfigurationLoader
import models.entities.Role
import models.generated.Tables.SystemUser
import org.specs2.execute.{AsResult, Result}

import org.specs2.mutable.{BeforeAfter, Specification}
import play.api.{Play, Application}
import play.api.db.evolutions.Evolutions
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.PlaySpecification
import play.db.Database
import repository.SystemUserRepo
import slick.driver.JdbcProfile

//import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.util.Random
import scala.concurrent.duration._

/**
 * Created by ipopkov on 02/08/15.
 */
class BaseDatabaseSpecification extends PlaySpecification with BeforeAfter {

  def builder = new GuiceApplicationBuilder()
    .configure(ConfigurationLoader.loadFirst("application.test.override.conf", "application.test.conf"))

  def application = builder.build()
  def injector = application.injector

    protected def afterEach(app: Application) = {
      recreateDbSchema(app)
    }

    private def recreateDbSchema(app: Application) = {
      val dbConfig = DatabaseConfigProvider.get[JdbcProfile](app)
      import dbConfig.driver.api._

      val recreateSchema: DBIO[Unit] = DBIO.seq(
        sqlu"drop schema public cascade",
        sqlu"create schema public"
      )

      println("before db clean")
      Await.ready(dbConfig.db.run(recreateSchema), 5 seconds)
    }

  override def before: Any = {}

  override def after: Any = {
    //FIXME: Type error here. Think about obatining a correct Database instance.
    //Evolutions.cleanupEvolutions(db)
  }
}
