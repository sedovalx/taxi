package controllers.auth

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import controllers.BaseController
import models.entities.User
import play.api.mvc.{BodyParsers, Action}
import utils.auth.Environment

class AuthController(val env: Environment)
  extends BaseController with Silhouette[User, JWTAuthenticator] {

  def authenticate = Action(BodyParsers.parse.json) { request =>
    Ok
  }

  def logOut = SecuredAction { request =>
    Ok
  }

}
