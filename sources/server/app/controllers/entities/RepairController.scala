package controllers.entities

import models.generated.Tables.{Repair, RepairFilter, RepairTable}
import repository.RepairRepo
import scaldi.Injector
import serialization.RepairSerializer
import service.RepairService

class RepairController(implicit injector: Injector)
  extends EntityController[Repair, RepairTable, RepairRepo, RepairFilter, RepairSerializer]()(injector){
  override protected val entityService = inject [RepairService]
  override protected val serializer: RepairSerializer = inject [RepairSerializer]

  override protected val entitiesName: String = "repairs"
  override protected val entityName: String = "repair"
}
