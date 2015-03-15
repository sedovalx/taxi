package controllers

import controllers.auth.AuthConfigImpl
import jp.t2v.lab.play2.auth.AuthenticationElement
import play.Play

/**
 * Контроллер, отдающий index.html клиентского приложения
 */
object IndexController extends BaseController with AuthenticationElement with AuthConfigImpl {

  def index(path: String) = StackAction { implicit request =>
    val file = Play.application().getFile("public/index.html")
    val source = scala.io.Source.fromFile(file)
    try {
      Ok(source.mkString).as("text/html")
    } finally source.close()
  }
}