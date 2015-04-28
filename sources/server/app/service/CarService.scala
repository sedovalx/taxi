package service

import models.generated.Tables
import models.generated.Tables.{Car, CarTable}
import repository.CarRepo
import utils.extensions.DateUtils

trait CarService extends EntityService[Car, CarTable, CarRepo]

class CarServiceImpl(val repo: CarRepo) extends CarService {
  override protected def setCreatorAndDate(entity: Tables.Car, creatorId: Option[Int]): Tables.Car =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override protected def setEditorAndDate(entity: Tables.Car, editorId: Option[Int]): Tables.Car =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override protected def setId(entity: Tables.Car, id: Int): Tables.Car = entity.copy(id = id)
}
