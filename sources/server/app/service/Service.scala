package service


import models.generated.Tables
import models.generated.Tables.{Driver, DriverTable, CarClass, CarClassTable}
import repository.{DriversRepo, CarClassRepo, GenericCRUD}
import utils.extensions.DateUtils

/**
 * Created by ipopkov on 14/04/15.
 */
trait CarClassService extends EntityService[CarClass, CarClassTable, GenericCRUD[CarClassTable, CarClass]]

class CarClassServiceImpl(carClassRepo: CarClassRepo) extends CarClassService {
  val repo = carClassRepo

  override def setCreatorAndDate(entity: CarClass, creatorId: Int) =
    entity.copy(creatorId = Some(creatorId), creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: CarClass, editorId: Int) =
    entity.copy(editorId = Some(editorId), editDate = Some(DateUtils.now))

  override def setId(entity: Tables.CarClass, id: Int): Tables.CarClass =
    entity.copy(id = id)
}


trait DriverService extends EntityService[Driver, DriverTable, GenericCRUD[DriverTable, Driver]]

class DriverServiceImpl(driverRepo: DriversRepo) extends DriverService {
  override val repo = driverRepo

  override def setCreatorAndDate(entity: Driver, creatorId: Int) =
    entity.copy(creatorId = Some(creatorId), creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Driver, editorId: Int) =
    entity.copy(editorId = Some(editorId), editDate = Some(DateUtils.now))

  override def setId(entity: Tables.Driver, id: Int): Tables.Driver =
    entity.copy(id = id)
}