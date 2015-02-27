package controllers

import play.api.mvc.{Action, Controller}
import utils.db.SchemaBuilder

object SchemaController extends Controller{
  def getDdlScript = Action {
    Ok(SchemaBuilder.getDdlScript)
  }
}
