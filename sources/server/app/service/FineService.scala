package service

import models.generated.Tables
import models.generated.Tables.{Fine, FineTable}
import repository.FineRepo
import utils.extensions.DateUtils

trait FineService extends EntityService[Fine, FineTable, FineRepo]

class FineServiceImpl(driverRepo: FineRepo) extends FineService {
  override val repo = driverRepo

  override def setCreatorAndDate(entity: Fine, creatorId: Option[Int]) =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Fine, editorId: Option[Int]) =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Tables.Fine, id: Int): Tables.Fine =
    entity.copy(id = id)
}