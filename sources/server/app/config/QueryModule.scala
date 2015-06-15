package config

import javax.inject.Singleton

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.{ScalaMultibinder, ScalaModule}
import query.{OperationQuery, RentQuery}
import service.query.{Query, QueryManagerImpl, QueryManager}

class QueryModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[QueryManager].to[QueryManagerImpl].in[Singleton]

    val queryBinder = ScalaMultibinder.newSetBinder[Query](binder)
    queryBinder.addBinding.to[RentQuery]
    queryBinder.addBinding.to[OperationQuery]
  }
}
