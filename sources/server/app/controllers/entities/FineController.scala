package controllers.entities

import models.generated.Tables.{Fine, FineFilter, FineTable}
import repository.FineRepo
import scaldi.Injector
import serialization.FineSerializer
import service.FineService

class FineController(implicit injector: Injector) extends EntityController[Fine, FineTable, FineRepo, FineFilter, FineSerializer]()(injector) {
  override protected val entityService = inject [FineService]
  override protected val serializer: FineSerializer = inject [FineSerializer]

  override protected val entitiesName: String = "fines"
  override protected val entityName: String = "fine"
}
