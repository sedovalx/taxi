package service

import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{RentStatus => RS, RentFilter, Rent, RentTable}
import repository.RentRepo
import utils.extensions.DateUtils

trait RentService extends EntityService[Rent, RentTable, RentRepo, RentFilter] {
  def getCurrentStatus(entity: Rent): RentStatus
  def createNewStatus(entity: Rent, status: RentStatus, editorId: Option[Int])
}

class RentServiceImpl(val repo: RentRepo, val statusService: RentStatusService) extends RentService {
  override def getCurrentStatus(entity: Rent): RentStatus = {
      statusService.getBy(entity.id).sorted(Ordering.by((i: RS) => i.changeTime.getTime).reverse).headOption match {
        case None => RentStatus.Active
        case Some(statusEntity) => statusEntity.status
      }
  }

  override def createNewStatus(entity: Tables.Rent, status: RentStatus, editorId: Option[Int]) = {
    val newStatus = RS(id = 0, changeTime = DateUtils.now, status = status, rentId = entity.id)
    statusService.create(newStatus, editorId)
  }
}
