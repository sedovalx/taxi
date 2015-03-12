package utils.db

import models._
import scala.slick.driver.PostgresDriver.simple._

object SchemaBuilder {
  def getDdlScript: String = {
    val statements = Users.objects.ddl.createStatements
    statements.mkString(";\n")
  }
}
