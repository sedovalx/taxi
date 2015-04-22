package queries

import org.specs2.mutable.Specification
import play.api.libs.json.JsArray

class QueryManagerTest extends Specification {

  class TestQuery(val name: String) extends Query {
    override def execute(parameters: Map[String, Seq[String]]): JsArray = JsArray()
  }

  "Query Manager" should {

    "return Query by name" >> {
      // prepare:
      val reportName = "test"
      val query = new TestQuery(reportName)
      val queryManager = new QueryManagerImpl(List(query))

      // test:
      val result = queryManager.getReport(reportName)

      // check:
      result must beSome
      result.get must beTheSameAs(query)
    }

    "return None if not found" >> {
      // prepare:
      val query = new TestQuery("test")
      val queryManager = new QueryManagerImpl(List(query))

      // test:
      val result = queryManager.getReport("some another name")

      // check:
      result must beNone
    }

  }
}
