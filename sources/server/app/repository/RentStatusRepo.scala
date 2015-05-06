package repository

import models.generated.Tables
import models.generated.Tables.{RentStatus, RentStatusTable}
import play.api.db.slick.Config.driver.simple._

trait RentStatusRepo extends GenericCRUD[RentStatus, RentStatusTable] {
  def getLastOf(rentId: Int)(implicit session: Session): Option[RentStatus]
}

class RentStatusRepoImpl extends RentStatusRepo {
  val tableQuery = RentStatusTable

  override def getLastOf(rentId: Int)(implicit session: Session): Option[Tables.RentStatus] = {
    tableQuery.filter(_.rentId === rentId).sortBy(_.changeDate.desc).run.headOption
  }
}