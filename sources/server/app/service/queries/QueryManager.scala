package service.queries

trait QueryManager {
  def getReport(name: String): Option[Query]
}

class QueryManagerImpl(reports: List[Query]) extends QueryManager {
  override def getReport(name: String): Option[Query] = {
    this.reports.find(r => r.name == name)
  }
}