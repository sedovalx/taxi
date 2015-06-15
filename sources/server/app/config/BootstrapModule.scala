package config

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import utils.AppBootstrapper

class BootstrapModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
//    bind[AppBootstrapper].asEagerSingleton()
  }
}
