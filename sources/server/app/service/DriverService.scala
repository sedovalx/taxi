package service

import models.generated.Tables
import models.generated.Tables.{Driver, DriverTable}
import repository.DriverRepo
import utils.extensions.DateUtils

trait DriverService extends EntityService[Driver, DriverTable, DriverRepo]

class DriverServiceImpl(driverRepo: DriverRepo) extends DriverService {
  override val repo = driverRepo

  override def setCreatorAndDate(entity: Driver, creatorId: Option[Int]) =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Driver, editorId: Option[Int]) =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Tables.Driver, id: Int): Tables.Driver =
    entity.copy(id = id)
}