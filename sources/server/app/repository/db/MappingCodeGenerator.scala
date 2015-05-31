package repository.db

import java.io.{BufferedWriter, FileWriter, File}

import scala.slick.model.Model

/**
 * Генератор маппинга по схеме БД
 * @example
 *          <pre>
 *          import repository.db.MappingCodeGenerator
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

      override def packageCode(profile: String, pkg: String, container:String="Tables") : String = {
        s"""
package ${pkg}
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object ${container} extends ${container}

trait ${container} {
  import ${profile}.simple._
  ${indent(code)}
}
      """.trim()
      }

      override def code = "import models.entities._" +
        "\n" + "import models.entities.Role.Role" +
        "\n" + "import repository.db.MappedColumnTypes._" +
        "\n" + "import com.mohiva.play.silhouette.api.Identity" +
        "\n" + "import utils.extensions.DateUtils" +
        "\n" + super.code

      override def entityName = (dbName: String) => dbName.toCamelCase

      override def tableName = (dbName: String) => dbName.toCamelCase + "Table"

      override def Table = new Table(_) {
        table =>

        override def PlainSqlMapper = new PlainSqlMapper {
          override def enabled = false
        }

        // формируем имена FK-свойств
        override def ForeignKey = new ForeignKey(_) {
          key =>
          override def rawName =
            key.referencingColumns.map{ _.name.toString.stripSuffix("Id") }.mkString + "Fk"
        }

        // для некоторых типов добавляем родителей
        override def EntityType = new EntityType {
          entity =>
          override def parents: Seq[String] = Seq(s"Entity[${entity.name.toString}]") ++ (entity.name.toString match {
            case "Account" => Seq("Identity")
            case "Payment" | "Fine" | "Repair" => Seq("BalanceChange")
            case _ => Nil
          })

          override def code = {
            val caseClassCode = super.code
            val caseClassAndCopyCode = caseClassCode +
            s"""
               |{
               |  def copyWithId(id: Int) = this.copy(id = id)
               |
               |  def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
               |
               |  def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
               |}
             """.stripMargin

            caseClassAndCopyCode + "\n" + entityFilterCode(caseClassCode) + "\n"
          }

          private def entityFilterCode(entityCode: String) = {
            val caseClassPattern = """case class (.*)\((.*)\)""".r.unanchored
            val argPartsPattern = """(.+): (.+?)(?:( = None)|$)""".r
            val Some(results) = entityCode match {
              case caseClassPattern(n, a) => Some((n, a))
              case _ => None
            }
            val name = results._1
            val args = results._2

            args.split(',').map { _.trim }.foldLeft(s"case class ${name}Filter(") ((agg, part) => {
              val parsedArgs = part match {
                case argPartsPattern(argName, argType, _) => Some((argName, argType))
                case _ => None
              }
              parsedArgs map { results =>
                val name = results._1
                val tpe = if (results._2.startsWith("Option")) results._2 else "Option[" + results._2 + "]"
                s"$name: $tpe = None"
              } match {
                case Some(x) => agg + x + ", "
                case _ => agg
              }
            }).dropRight(2) + ")"
          }
        }

        // генерация кода для столбцов в маппинге
        override def Column = new Column(_) {
          column =>
          // общий вид столбца
          override def code = s"""val $name = column[$actualType]("${model.name}"${options.map(", "+_).mkString("")})"""

          override def rawType: String = {
            // для столбца role из AccountTable указываем, что нужно использовать тип Role
            // аналогичный тип будет использован и в entity Account
            if (table.TableClass.name.toString == "AccountTable" && column.name.toString == "role") parseType("Role")
            // аналогично для rent_status.status
            else if (table.TableClass.name.toString == "RentStatusTable" && column.name.toString == "status") parseType("models.entities.RentStatus.RentStatus")
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
