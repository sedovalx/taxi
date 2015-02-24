package utils.db

import models._
import scala.slick.driver.PostgresDriver.simple._

object SchemaBuilder {
  def getDdlScript: String = {
    val statements = CarClasses.objects.ddl.createStatements ++ Cars.objects.ddl.createStatements
    statements.mkString(";\n")
  }
}
