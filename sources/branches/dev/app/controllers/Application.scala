package controllers

import play.api.mvc._

object Application extends Controller {

  def index(path: String) = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}