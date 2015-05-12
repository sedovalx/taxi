package controllers.entities

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Fine, FineTable}
import play.api.libs.json.{Reads, Writes}
import repository.FineRepo
import scaldi.Injector
import service.EntityService

class FineController(implicit injector: Injector) extends EntityController[Fine, FineTable, FineRepo] {
  override protected val entityService: EntityService[Tables.Fine, Tables.FineTable, FineRepo] = _

  override protected def copyEntityWithId(entity: Tables.Fine, id: Int): Tables.Fine = ???

  override protected val entitiesName: String = "fines"
  override protected implicit val reads: Reads[Tables.Fine] = _
  override protected implicit val writes: Writes[Tables.Fine] = _
  override protected val entityName: String = "fine"

  override protected def env: Environment[Tables.Account, JWTAuthenticator] = ???
}
