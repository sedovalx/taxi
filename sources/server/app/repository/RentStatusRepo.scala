package repository

import models.generated.Tables.{RentStatus, RentStatusTable}
import play.api.db.slick.Config.driver.simple._

trait RentStatusRepo extends GenericCRUD[RentStatus, RentStatusTable] {
  def getBy(rentId: Int)(implicit session: Session): Seq[RentStatus]
}

class RentStatusRepoImpl extends RentStatusRepo {
  val tableQuery = RentStatusTable

  override def getBy(rentId: Int)(implicit session: Session): Seq[RentStatus] = {
    tableQuery.filter(_.rentId === rentId).run
  }
}