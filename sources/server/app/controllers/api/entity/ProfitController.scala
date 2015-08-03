package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Profit, ProfitFilter, ProfitTable}
import play.api.i18n.MessagesApi
import repository.ProfitRepo
import serialization.entity.ProfitSerializer
import service.entity.ProfitService

class ProfitController @Inject()(
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: ProfitService,
  val serializer: ProfitSerializer)
  extends EntityController[Profit, ProfitTable, ProfitRepo, ProfitFilter, ProfitSerializer] {
  override protected val entityName: String = "profit"
  override protected val entitiesName: String = "profits"
}
