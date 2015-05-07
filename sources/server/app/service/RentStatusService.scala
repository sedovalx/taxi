package service

import models.generated.Tables
import models.generated.Tables.{RentStatus, RentStatusTable}
import repository.RentStatusRepo
import utils.extensions.DateUtils

trait RentStatusService extends EntityService[RentStatus, RentStatusTable, RentStatusRepo] {
  def getBy(rentId: Int): Seq[RentStatus]
}

class RentStatusServiceImpl(rentStatusRepo: RentStatusRepo) extends RentStatusService {
  override val repo = rentStatusRepo

  override def setCreatorAndDate(entity: RentStatus, creatorId: Option[Int]) =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: RentStatus, editorId: Option[Int]) =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Tables.RentStatus, id: Int): Tables.RentStatus =
    entity.copy(id = id)

  override def getBy(rentId: Int): Seq[Tables.RentStatus] = {
    withDb { session => repo.getBy(rentId)(session) }
  }
}