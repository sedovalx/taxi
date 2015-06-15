package service.query

import javax.inject.Inject

trait QueryManager {
  def getReport(name: String): Option[Query]
}

class QueryManagerImpl @Inject() (reports: Set[Query]) extends QueryManager {
  override def getReport(name: String): Option[Query] = {
    this.reports.find(r => r.name == name)
  }
}
