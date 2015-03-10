package controllers

import java.io.File

import play.Play
import play.api.mvc._

object Application extends Controller {

  def index(path: String) = Action {
    val projectRoot = Play.application().path()
    val file = new File(projectRoot + "/public/index.html")
    Ok(scala.io.Source.fromFile(file.getCanonicalPath).mkString).as("text/html")
  }
}