package config

import javax.inject.Singleton

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import service.entity._
import service.query.{QueryManagerImpl, QueryManager}

class ServiceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CarService].to[CarServiceImpl].in[Singleton]
    bind[DriverService].to[DriverServiceImpl].in[Singleton]
    bind[OperationService].to[OperationServiceImpl].in[Singleton]
    bind[RentStatusService].to[RentStatusServiceImpl].in[Singleton]
    bind[RentService].to[RentServiceImpl].in[Singleton]
    bind[SystemUserService].to[SystemUserServiceImpl].in[Singleton]
    bind[RefundService].to[RefundServiceImpl].in[Singleton]
    bind[ProfitService].to[ProfitServiceImpl].in[Singleton]
  }
}
