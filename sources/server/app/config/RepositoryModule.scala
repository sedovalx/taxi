package config

import javax.inject.Singleton

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import repository._

class RepositoryModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CarRepo].to[CarRepo].in[Singleton]
    bind[DriverRepo].to[DriverRepo].in[Singleton]
    bind[RentRepo].to[RentRepo].in[Singleton]
    bind[RentStatusRepo].to[RentStatusRepo].in[Singleton]
    bind[OperationRepo].to[OperationRepo].in[Singleton]
    bind[SystemUserRepo].to[SystemUserRepo].in[Singleton]
  }
}
