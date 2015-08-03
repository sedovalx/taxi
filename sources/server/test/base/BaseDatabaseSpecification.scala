package base

import config.ConfigurationLoader
import org.specs2.specification.BeforeAfterEach
import play.api.db.DBApi
import play.api.db.evolutions.Evolutions
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.PlaySpecification

/**
 * Created by ipopkov on 02/08/15.
 */
class BaseDatabaseSpecification extends PlaySpecification with BeforeAfterEach {

  def builder = new GuiceApplicationBuilder()
    .configure(ConfigurationLoader.loadFirst("application.test.override.conf", "application.test.conf"))

  def application = builder.build()
  def injector = application.injector
//
//    protected def afterEach(app: Application) = {
//      recreateDbSchema(app)
//    }
//
//    private def recreateDbSchema(app: Application) = {
//      val dbConfig = DatabaseConfigProvider.get[JdbcProfile](app)
//      import dbConfig.driver.api._
//
//      val recreateSchema: DBIO[Unit] = DBIO.seq(
//        sqlu"drop schema public cascade",
//        sqlu"create schema public"
//      )
//
//      println("before db clean")
//      Await.ready(dbConfig.db.run(recreateSchema), 5 seconds)
//    }

  override def before: Any = {
    val dbapi = application.injector.instanceOf[DBApi]
    Evolutions.applyEvolutions(dbapi.database("default"))
  }

  override def after: Any = {
    //FIXME: Type error here. Think about obatining a correct Database instance.
    //Evolutions.cleanupEvolutions(db)
    val dbapi = application.injector.instanceOf[DBApi]
    Evolutions.cleanupEvolutions(dbapi.database("default"))
  }
}
