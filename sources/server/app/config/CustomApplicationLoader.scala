package config

import com.typesafe.config.ConfigFactory
import play.api.ApplicationLoader
import play.api.Configuration
import play.api.inject.guice._

class CustomApplicationLoader extends GuiceApplicationLoader() {
  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
    val extra = new Configuration(loadConfig)
    initialBuilder
      .in(context.environment)
      .loadConfig(context.initialConfiguration ++ extra)
      .overrides(overrides(context): _*)
  }

  def loadConfig = {
    val extraConfFile = "application.override.conf"
    val confFileName = if (getClass.getResource("/" + extraConfFile) != null) extraConfFile else "application.conf"
    ConfigFactory.load(confFileName)
  }
}
