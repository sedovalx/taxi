package config

import com.typesafe.config.ConfigFactory
import play.api.Configuration

object ConfigurationLoader {
  def loadFirst(configFileNames: String*): Configuration = {
    val confFileName = configFileNames.find(name => getClass.getResource("/" + name) != null).getOrElse("application.conf")
    val config = ConfigFactory.load(confFileName)
    new Configuration(config)
  }
}
