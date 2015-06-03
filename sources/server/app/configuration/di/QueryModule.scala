package configuration.di

import scaldi.Module
import service.queries.{CashierQuery, Query}

class QueryModule extends Module {
  bind [Query] to new CashierQuery
}
