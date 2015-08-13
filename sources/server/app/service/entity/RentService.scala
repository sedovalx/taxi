package service.entity

import javax.inject.Inject

import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{RentStatus => RS, RentFilter, Rent, RentTable}
import repository.RentRepo
import utils.DateUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait RentService extends EntityService[Rent, RentTable, RentRepo, RentFilter] {
  def getCurrentStatus(entity: Rent): Future[RentStatus]
  def createNewStatus(entity: Rent, status: RentStatus, editorId: Int): Future[Tables.RentStatus]
}

class RentServiceImpl @Inject() (val repo: RentRepo, val statusService: RentStatusService) extends RentService {
  override def getCurrentStatus(entity: Rent): Future[RentStatus] = {
      statusService.getBy(entity.id) map { statuses =>
        statuses.sorted(Ordering.by((i: RS) => i.changeTime.getTime).reverse).headOption match {
          case None => RentStatus.Active
          case Some(statusEntity) => statusEntity.status
        }
      }
  }

  override def createNewStatus(entity: Tables.Rent, status: RentStatus, editorId: Int) = {
    val newStatus = RS(id = 0, changeTime = DateUtils.now, status = status, rentId = entity.id)
    statusService.create(newStatus, editorId)
  }
}
