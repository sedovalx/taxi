package t

import models.generated.Tables
import models.generated.Tables.{CarClass, CarClassTable}
import utils.db.repo.GenericCRUD
import utils.extensions.DateUtils

trait CarClassService extends EntityService[CarClass, CarClassTable, GenericCRUD[CarClassTable, CarClass]]

class CarClassServiceImpl extends CarClassService {
  override val repo = CarClassTable

  override def setCreatorAndDate(entity: CarClass, creatorId: Int) =
    entity.copy(creatorId = Some(creatorId), creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: CarClass, editorId: Int) =
    entity.copy(editorId = Some(editorId), editDate = Some(DateUtils.now))

  override def setId(entity: Tables.CarClass, id: Int): Tables.CarClass =
    entity.copy(id = id)
}

