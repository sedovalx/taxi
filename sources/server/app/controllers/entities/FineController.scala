package controllers.entities

import models.generated.Tables
import models.generated.Tables.{Fine, FineTable}
import play.api.libs.json.{Reads, Writes}
import repository.FineRepo
import scaldi.Injector
import service.FineService

class FineController(implicit injector: Injector) extends EntityController[Fine, FineTable, FineRepo]()(injector) {
  override protected val entityService = inject [FineService]

  override protected val entitiesName: String = "fines"
  override protected val entityName: String = "fine"

  override protected implicit val reads: Reads[Tables.Fine] = ???
  override protected implicit val writes: Writes[Tables.Fine] = ???
}
