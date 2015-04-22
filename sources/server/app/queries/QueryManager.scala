package queries

trait QueryManager {
  def getReport(name: String): Option[Query]
}
