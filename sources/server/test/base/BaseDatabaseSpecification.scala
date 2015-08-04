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
