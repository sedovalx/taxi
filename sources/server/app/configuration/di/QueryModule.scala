package configuration.di

import scaldi.Module
import service.queries.{RentQuery, PaymentsQuery, CashierQuery, Query}

class QueryModule extends Module {
  bind [Query] to new CashierQuery
  bind [Query] to new PaymentsQuery
  bind [Query] to new RentQuery
}
