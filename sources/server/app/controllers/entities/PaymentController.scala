package controllers.entities

import models.generated.Tables.{Payment, PaymentFilter, PaymentTable}
import repository.PaymentRepo
import scaldi.Injector
import serialization.PaymentSerializer
import service.PaymentService

class PaymentController(implicit injector: Injector)
  extends EntityController[Payment, PaymentTable, PaymentRepo, PaymentFilter, PaymentSerializer]()(injector) {
  override protected val entityService = inject [PaymentService]
  override protected val serializer: PaymentSerializer = inject [PaymentSerializer]

  override protected val entitiesName: String = "payments"
  override protected val entityName: String = "payment"
}
