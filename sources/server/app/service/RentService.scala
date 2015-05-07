package service

import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Rent, RentStatus => RS, RentTable}
import repository.RentRepo
import utils.extensions.DateUtils

trait RentService extends EntityService[Rent, RentTable, RentRepo] {
  def getCurrentStatus(entity: Rent): RentStatus
  def createNewStatus(entity: Rent, status: RentStatus, editorId: Option[Int])
}

class RentServiceImpl(val repo: RentRepo, val statusService: RentStatusService) extends RentService {
  override protected def setCreatorAndDate(entity: Tables.Rent, creatorId: Option[Int]): Tables.Rent =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override protected def setEditorAndDate(entity: Tables.Rent, editorId: Option[Int]): Tables.Rent =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override protected def setId(entity: Tables.Rent, id: Int): Tables.Rent = entity.copy(id = id)

  override def getCurrentStatus(entity: Rent): RentStatus = {
      statusService.getBy(entity.id).sorted(Ordering.by((i: RS) => i.changeDate.getTime).reverse).headOption match {
        case None => RentStatus.Active
        case Some(statusEntity) => statusEntity.status
      }
  }

  override def createNewStatus(entity: Tables.Rent, status: RentStatus, editorId: Option[Int]) = {
    val newStatus = RS(id = 0, changeDate = DateUtils.now, status = status, rentId = entity.id)
    statusService.create(newStatus, editorId)
  }
}
