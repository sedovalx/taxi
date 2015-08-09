package controllers.api.entity

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.generated.Tables
import models.generated.Tables.{Profit, ProfitFilter, ProfitTable}
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import repository.ProfitRepo
import serialization.entity.ProfitSerializer
import service.entity.ProfitService

import scala.concurrent.ExecutionContext.Implicits.global

class ProfitController @Inject()(
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  val entityService: ProfitService,
  val serializer: ProfitSerializer)
  extends EntityController[Profit, ProfitTable, ProfitRepo, ProfitFilter, ProfitSerializer] {
  override protected val entityName: String = "profit"
  override protected val entitiesName: String = "profits"

  def currentState = SecuredAction.async { implicit request =>
    logger.debug(s"Запрос на получение суммы в кассе: $request")
    entityService.getCurrentState map { amount =>
      logger.debug(s"Ответ на запрос суммы в кассе: $amount")
      Ok(Json.obj("amount" -> amount))
    }
  }
}
