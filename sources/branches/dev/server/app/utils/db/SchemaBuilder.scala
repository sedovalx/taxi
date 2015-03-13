package utils.db

import models._
import models.repos.UsersRepo
import scala.slick.driver.PostgresDriver.simple._

object SchemaBuilder {
  def getDdlScript: String = {
    val statements = UsersRepo.objects.ddl.createStatements
    statements.mkString(";\n")
  }
}
