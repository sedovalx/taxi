package controllers.api.tools

import java.nio.file.Paths
import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import utils.slick.SourceCodeGenerator

class SchemaController @Inject() (generator: SourceCodeGenerator) extends Controller {
  def regenerateTableCode = Action {

    generator.run(
      slickDriver = "slick.driver.PostgresDriver",
      jdbcDriver = "org.postgresql.Driver",
      url = "jdbc:postgresql://localhost:5432/taxi",
      outputDir = Paths.get(play.Play.application().path().getAbsolutePath, "app").toString,
      pkg = "models.generated.draft",
      user = "postgres",
      password = "11111"
    )
    Ok("ok")
  }
}
