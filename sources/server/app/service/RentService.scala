package service

import models.entities.RentStatus
import models.entities.RentStatus.RentStatus
import models.generated.Tables
import models.generated.Tables.{Rent, RentTable}
import repository.{RentStatusRepo, RentRepo}
import utils.extensions.DateUtils

trait RentService extends EntityService[Rent, RentTable, RentRepo] {
//  def getCurrentStatus(entity: Rent): RentStatus
}

class RentServiceImpl(val repo: RentRepo, val statusRepo: RentStatusRepo) extends RentService {
  override protected def setCreatorAndDate(entity: Tables.Rent, creatorId: Option[Int]): Tables.Rent =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override protected def setEditorAndDate(entity: Tables.Rent, editorId: Option[Int]): Tables.Rent =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override protected def setId(entity: Tables.Rent, id: Int): Tables.Rent = entity.copy(id = id)

//  override def getCurrentStatus(entity: Rent): RentStatus = {
//    withDb { session =>
//      statusRepo.getLastOf(entity.id)(session) match {
//        case None => RentStatus.Active
//        case Some(statusEntity) => statusEntity.status
//      }
//    }
//  }
}
