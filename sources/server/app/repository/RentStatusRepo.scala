package repository

import models.generated.Tables
import models.generated.Tables.{RentStatusFilter, RentStatus, RentStatusTable}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future

class RentStatusRepo extends GenericCRUDImpl[RentStatus, RentStatusTable, RentStatusFilter] {
  def getBy(rentId: Int): Future[Seq[Tables.RentStatus]] = {
    db.run(tableQuery.filter(_.rentId === rentId).result)
  }

  override val tableQuery: PostgresDriver.api.TableQuery[Tables.RentStatusTable] = RentStatusTable
}
