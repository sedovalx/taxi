package service

import models.generated.Tables
import models.generated.Tables.{CarClass, CarClassTable}
import repository.CarClassRepo
import utils.extensions.DateUtils

trait CarClassService extends EntityService[CarClass, CarClassTable, CarClassRepo]

class CarClassServiceImpl(carClassRepo: CarClassRepo) extends CarClassService {
  val repo = carClassRepo

  override def setCreatorAndDate(entity: CarClass, creatorId: Option[Int]) =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: CarClass, editorId: Option[Int]) =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Tables.CarClass, id: Int): Tables.CarClass =
    entity.copy(id = id)
}