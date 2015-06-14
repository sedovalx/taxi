package controllers

import play.Play
import play.api.mvc.{Controller, Action}

/**
 * Контроллер, отдающий index.html клиентского приложения
 */
class IndexController extends Controller {

  def index(path: String) = Action { implicit request =>
    val file = Play.application().getFile("public/index.html")
    val source = scala.io.Source.fromFile(file)
    try {
      Ok(source.mkString).as("text/html")
    } finally source.close()
  }
}
