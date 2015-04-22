package queries

class QueryManagerImpl(reports: List[Query]) extends QueryManager {
  override def getReport(name: String): Option[Query] = {
    this.reports.find(r => r.name == name)
  }
}
