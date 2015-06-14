package config

import javax.inject.Singleton

import com.google.inject.{Provides, AbstractModule}
import net.codingwell.scalaguice.ScalaModule
import play.api.db.slick.DatabaseConfigProvider
import repository._
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

class RepositoryModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CarRepo].in[Singleton]
    bind[DriverRepo].in[Singleton]
    bind[RentRepo].in[Singleton]
    bind[RentStatusRepo].in[Singleton]
    bind[OperationRepo].in[Singleton]
    bind[SystemUserRepo].in[Singleton]
  }

  @Provides
  def provideDatabase(dbConfigProvider: DatabaseConfigProvider): Database = {
    dbConfigProvider.get[JdbcProfile].db
  }
}
