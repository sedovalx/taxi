package config

import javax.inject.Singleton

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.{ScalaModule, ScalaMultibinder}
import query.{RentHistoryQuery, CashierQuery, RentQuery}
import service.query.{Query, QueryManager, QueryManagerImpl}

class QueryModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[QueryManager].to[QueryManagerImpl].in[Singleton]

    val queryBinder = ScalaMultibinder.newSetBinder[Query](binder)
    queryBinder.addBinding.to[RentQuery]
    queryBinder.addBinding.to[CashierQuery]
    queryBinder.addBinding.to[RentHistoryQuery]
  }
}
