package controllers.entities

import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.generated.Tables
import models.generated.Tables.Account
import utils.TestModelGenerator
import utils.auth.WithRole
import scala.concurrent.ExecutionContext.Implicits.global

class TestController(val env: Environment[Tables.Account, JWTAuthenticator], generator: TestModelGenerator) extends BaseController with Silhouette[Account, JWTAuthenticator] {
  def regenerateModel = SecuredAction(WithRole(models.entities.Role.Administrator)).async {
    generator.generate() map { _ => Ok("ok")}
  }
}
