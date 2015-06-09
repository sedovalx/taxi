package configuration.di

import scaldi.Module
import service.queries._

class QueryModule extends Module {
  bind [Query] to new CashierQuery
  bind [Query] to new OperationQuery
  bind [Query] to new RentQuery
  bind [Query] to new IvanTestQuery
}
