package query

import javax.inject.Inject

import play.api.libs.json._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.GetResult
import utils.DateUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RentTotalQuery @Inject()(dbConfig: DatabaseConfig[JdbcProfile]) extends SqlRentQuery(dbConfig) {
  override val name: String = "q-rent-total"

  private implicit val writes = Json.writes[RentTotal]
  private implicit val getResult = GetResult(r => RentTotal(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  case class RentTotal(rentId: Int, minutes: Int, payments: BigDecimal, repairs: BigDecimal, fines: BigDecimal, deposit: BigDecimal, total: BigDecimal)

  override protected def doExecute(parameters: Map[String, Seq[String]]): Future[JsValue] = {
    val filter = parseFilter(parameters)

    val sql = readSql(Map(
      "@rentId" -> filter.rent.toString,
      "@controlTime" -> DateUtils.iso8601Format.format(filter.date.get)
    ))
    fetchResult[RentTotal](sql) map { total =>
      if (total.isEmpty){
        Json.obj()
      } else {
        Json.toJson(total.head)
      }
    }
  }
}
