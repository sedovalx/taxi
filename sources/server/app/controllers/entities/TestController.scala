package controllers.entities

import controllers.BaseController
import play.api.mvc.Action
import utils.TestModelGenerator
import scala.concurrent.ExecutionContext.Implicits.global

class TestController(generator: TestModelGenerator) extends BaseController {
  def regenerateModel = Action.async {
    generator.generate() map { _ => Ok("ok")}
  }
}
