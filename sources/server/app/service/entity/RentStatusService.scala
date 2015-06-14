package service.entity

import models.generated.Tables
import models.generated.Tables.{RentStatusFilter, RentStatus, RentStatusTable}
import repository.RentStatusRepo

import scala.concurrent.Future

trait RentStatusService extends EntityService[RentStatus, RentStatusTable, RentStatusRepo, RentStatusFilter] {
  def getBy(rentId: Int): Future[Seq[RentStatus]]
}

class RentStatusServiceImpl(val repo: RentStatusRepo) extends RentStatusService {
  override def getBy(rentId: Int): Future[Seq[Tables.RentStatus]] = {
    repo.getBy(rentId)
  }
}
