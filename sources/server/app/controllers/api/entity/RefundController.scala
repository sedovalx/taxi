package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Refund, RefundFilter, RefundTable}
import play.api.i18n.MessagesApi
import repository.RefundRepo
import serialization.entity.RefundSerializer
import service.entity.RefundService

class RefundController @Inject()(
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: RefundService,
  val serializer: RefundSerializer)
  extends EntityController[Refund, RefundTable, RefundRepo, RefundFilter, RefundSerializer] {
  override protected val entityName: String = "refund"
  override protected val entitiesName: String = "refunds"
}
