package service

import models.generated.Tables
import models.generated.Tables.{Payment, PaymentTable}
import repository.PaymentRepo
import utils.extensions.DateUtils

trait PaymentService extends EntityService[Payment, PaymentTable, PaymentRepo]

class PaymentServiceImpl(driverRepo: PaymentRepo) extends PaymentService {
  override val repo = driverRepo

  override def setCreatorAndDate(entity: Payment, creatorId: Option[Int]) =
    entity.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))

  override def setEditorAndDate(entity: Payment, editorId: Option[Int]) =
    entity.copy(editorId = editorId, editDate = Some(DateUtils.now))

  override def setId(entity: Tables.Payment, id: Int): Tables.Payment =
    entity.copy(id = id)
}