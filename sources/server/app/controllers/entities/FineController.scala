package controllers.entities

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Fine, FineTable}
import play.api.libs.json.{Reads, Writes}
import repository.FineRepo
import scaldi.Injector
import service.{FineService, EntityService}

class FineController(implicit injector: Injector) extends EntityController[Fine, FineTable, FineRepo]()(injector) {
  override protected val entityService = inject [FineService]

  override protected def copyEntityWithId(entity: Tables.Fine, id: Int): Tables.Fine = entity.copy(id = id)

  override protected val entitiesName: String = "fines"
  override protected val entityName: String = "fine"

  override protected implicit val reads: Reads[Tables.Fine] = ???
  override protected implicit val writes: Writes[Tables.Fine] = ???
}
