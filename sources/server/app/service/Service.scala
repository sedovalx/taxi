package service


import models.generated.Tables
import models.generated.Tables.{Driver, DriverTable, CarClass, CarClassTable}
import repository.{DriversRepo, CarClassRepo, GenericCRUD}
import utils.extensions.DateUtils

/**
 * Created by ipopkov on 14/04/15.
 */
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


trait DriverService extends EntityService[Driver, DriverTable, DriversRepo]

class DriverServiceImpl(driverRepo: DriversRepo) extends DriverService {
  override val repo = driverRepo

  override def setCreatorAndDate(entity: Driver, creatorId: Option[Int]) =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Driver, editorId: Option[Int]) =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Tables.Driver, id: Int): Tables.Driver =
    entity.copy(id = id)
}