package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.entities.Role
import models.generated.Tables
import models.generated.Tables.{Operation, OperationFilter, OperationTable}
import play.api.i18n.MessagesApi
import play.api.mvc.BodyParsers
import repository.OperationRepo
import serialization.entity.OperationSerializer
import service.entity.OperationService
import utils.auth.WithRole

class OperationController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: OperationService,
  val serializer: OperationSerializer)
  extends EntityController[Operation, OperationTable, OperationRepo, OperationFilter, OperationSerializer] {
  override protected val entitiesName: String = "operations"
  override protected val entityName: String = "operation"

  override def update(id: Int) = SecuredAction(WithRole(Role.Administrator)).async(BodyParsers.parse.json) { implicit request =>
    super.doUpdate(id, request)
  }
}
