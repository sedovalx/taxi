package controllers.auth

import controllers.BaseController
import jp.t2v.lab.play2.auth.LoginLogout
import models.repos.UsersRepo
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action
import views.html

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LoginController extends BaseController with LoginLogout with AuthConfigImpl {
  /** Your application's login form.  Alter it to fit your application */
  val loginForm = {
    withDb { implicit session =>
      Form {
        mapping("login" -> text, "password" -> text)(UsersRepo.authenticate)(_.map(u => (u.login, "")))
          .verifying("Invalid login or password", result => result.isDefined)
      }
    }
  }

  /** Alter the login page action to suit your application. */
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  /**
   * Return the `gotoLogoutSucceeded` method's result in the logout action.
   *
   * Since the `gotoLogoutSucceeded` returns `Future[Result]`,
   * you can add a procedure like the following.
   *
   *   gotoLogoutSucceeded.map(_.flashing(
   *     "success" -> "You've been logged out"
   *   ))
   */
  def logout = Action.async { implicit request =>
    // do something...
    gotoLogoutSucceeded.map(_.flashing(
      "success" -> "You've been logged out"
    ))
  }

  /**
   * Return the `gotoLoginSucceeded` method's result in the login action.
   *
   * Since the `gotoLoginSucceeded` returns `Future[Result]`,
   * you can add a procedure like the `gotoLogoutSucceeded`.
   */
  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.login(formWithErrors))),
      user => gotoLoginSucceeded(user.get.id)
    )
  }
}
