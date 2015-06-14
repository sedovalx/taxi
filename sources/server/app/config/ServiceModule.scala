package config

import javax.inject.Singleton

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import service.entity._

class ServiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CarService].to[CarServiceImpl].in[Singleton]
    bind[DriverService].to[DriverServiceImpl].in[Singleton]
    bind[OperationService].to[OperationServiceImpl].in[Singleton]
  }
}
