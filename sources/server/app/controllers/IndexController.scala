package controllers

import java.io.File

import controllers.auth.AuthConfigImpl
import jp.t2v.lab.play2.auth.AuthenticationElement
import play.Play

/**
 * Контроллер, отдающий index.html клиентского приложения
 */
object IndexController extends BaseController with AuthenticationElement with AuthConfigImpl {

  def index(path: String) = StackAction { implicit request =>
    val user = loggedIn
    println(s"Logged user: ${user.login}")
    val projectRoot = Play.application().path()
    val file = new File(projectRoot + "/public/index.html")
    Ok(scala.io.Source.fromFile(file.getCanonicalPath).mkString).as("text/html")
  }
}