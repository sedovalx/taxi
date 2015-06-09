package controllers.entities

import models.generated.Tables.{Operation, OperationFilter, OperationTable}
import repository.OperationRepo
import scaldi.Injector
import serialization.OperationSerializer
import service.OperationService

class OperationController(implicit injector: Injector)
  extends EntityController[Operation, OperationTable, OperationRepo, OperationFilter, OperationSerializer]()(injector) {
  override protected val entityService = inject [OperationService]
  override protected val serializer: OperationSerializer = inject [OperationSerializer]

  override protected val entitiesName: String = "operations"
  override protected val entityName: String = "operation"
}
