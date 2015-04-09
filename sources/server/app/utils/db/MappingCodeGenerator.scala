package utils.db

import java.io.{BufferedWriter, FileWriter, File}

import scala.slick.model.Model

/**
 * Генератор маппинга по схеме БД
 * @example
 *          <pre>
 *          import utils.db.MappingCodeGenerator
 *          import scala.slick.driver.PostgresDriver
 *
 *          Class.forName("org.postgresql.Driver")
 *          val db = PostgresDriver.simple.Database.forURL("jdbc:postgresql://localhost:5433/taxi", "user", "password")
 *          val model = db.withSession { implicit session =>
 *            PostgresDriver.createModel()
 *          }
 *          MappingCodeGenerator.save(model, "models.generated", "path_to_\\taxi\\sources\\server\\app\\")
 *          </pre>
 */
object MappingCodeGenerator {
  def apply(model: Model, packageName: String): String = {
    val codeGen = new scala.slick.codegen.SourceCodeGenerator(model){

      override def code = "import models.entities._" +
        "\n" + "import models.entities.Role._" +
        "\n" + "import com.mohiva.play.silhouette.api.Identity" +
        "\n" + super.code

      override def entityName = (dbName: String) => dbName.toCamelCase

      override def tableName = (dbName: String) => dbName.toCamelCase + "Table"

      override def Table = new Table(_) {
        table =>
        override def ForeignKey = new ForeignKey(_) {
          key =>
          override def rawName =
            key.referencingColumns.map{ _.name.toString.stripSuffix("Id") }.mkString + "Fk"
        }

        override def EntityType = new EntityType {
          entity =>
          override def parents: Seq[String] = Seq("Entity") ++ (if (entity.name.toString == "Account") Seq("Identity") else Seq())
        }

        override def Column = new Column(_) {
          column =>
          override def code = s"""val $name = column[$actualType]("${model.name}"${options.map(", "+_).mkString("")})"""
          override def rawType: String = {
            if (table.TableClass.name.toString == "Account" && column.name.toString == "role") parseType("Role")
            else super.rawType.toString
          }
        }
      }
    }

    codeGen.packageCode("play.api.db.slick.Config.driver", packageName)
  }

  def save(model: Model, packageName: String, folder: String) = {
    val code = apply(model, packageName)
    val folder2 : String = folder + "/" + packageName.replace(".", "/") + "/"
    new File(folder2).mkdirs()
    val file = new File( folder2 + "Tables.scala" )
    if (!file.exists()) {
      file.createNewFile()
    }
    val fw = new FileWriter(file.getAbsoluteFile)
    val bw = new BufferedWriter(fw)
    bw.write(code)
    bw.close()
  }
}
