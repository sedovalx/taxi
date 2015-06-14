package utils.config

import java.net.InetAddress

import com.typesafe.config.ConfigFactory
import play.api.Configuration

object ConfigHelper {
  def loadMachineSpecific(resourceName: String, resourceExt: String = "conf") = {
    val host = InetAddress.getLocalHost
    val hostName = host.getHostName
    Configuration(ConfigFactory.load(s"$resourceName.$hostName.override.$resourceExt"))
  }
}
