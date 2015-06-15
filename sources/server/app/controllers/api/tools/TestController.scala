package controllers.api.tools

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.api.BaseController
import models.generated.Tables
import play.api.i18n.MessagesApi
import utils.auth.WithRole
import utils.slick.TestModelGenerator

import scala.concurrent.ExecutionContext.Implicits.global

class TestController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[Tables.SystemUser, JWTAuthenticator],
  generator: TestModelGenerator)
  extends BaseController {
  def regenerateModel = SecuredAction(WithRole(models.entities.Role.Administrator)).async {
    generator.generate() map { _ => Ok("ok")}
  }
}
