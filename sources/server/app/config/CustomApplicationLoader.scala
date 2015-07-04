package config

import play.api.ApplicationLoader
import play.api.inject.guice._

class CustomApplicationLoader extends GuiceApplicationLoader() {

  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
    val extra = ConfigurationLoader.loadFirst("application.override.conf")
    initialBuilder
      .in(context.environment)
      .loadConfig(context.initialConfiguration ++ extra)
      .overrides(overrides(context): _*)
  }

}


