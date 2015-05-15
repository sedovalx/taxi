package service

import models.generated.Tables
import models.generated.Tables.{RentStatusFilter, RentStatus, RentStatusTable}
import repository.RentStatusRepo
import utils.extensions.DateUtils

trait RentStatusService extends EntityService[RentStatus, RentStatusTable, RentStatusRepo, RentStatusFilter] {
  def getBy(rentId: Int): Seq[RentStatus]
}

class RentStatusServiceImpl(val repo: RentStatusRepo) extends RentStatusService {
  override def getBy(rentId: Int): Seq[Tables.RentStatus] = {
    withDb { session => repo.getBy(rentId)(session) }
  }
}