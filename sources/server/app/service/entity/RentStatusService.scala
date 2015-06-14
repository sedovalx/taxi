package service.entity

import javax.inject.Inject

import models.generated.Tables
import models.generated.Tables.{RentStatusFilter, RentStatus, RentStatusTable}
import repository.RentStatusRepo

import scala.concurrent.Future

trait RentStatusService extends EntityService[RentStatus, RentStatusTable, RentStatusRepo, RentStatusFilter] {
  def getBy(rentId: Int): Future[Seq[RentStatus]]
}

class RentStatusServiceImpl @Inject() (val repo: RentStatusRepo) extends RentStatusService {
  override def getBy(rentId: Int): Future[Seq[Tables.RentStatus]] = {
    repo.getBy(rentId)
  }
}
