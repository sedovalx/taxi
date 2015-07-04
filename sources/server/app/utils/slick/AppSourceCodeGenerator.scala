package utils.slick

class AppSourceCodeGenerator(model: slick.model.Model) extends slick.codegen.SourceCodeGenerator(model) {

  /** Generates code for the complete model (not wrapped in a package yet)
      @group Basic customization overrides */
  override def code = {
    "import models.entities._" +
      "\n" + "import models.entities.Role.Role" +
      "\n" + "import db.MappedColumnTypes._" +
      "\n" + "import com.mohiva.play.silhouette.api.Identity" +
      "\n" + "import utils.DateUtils" +
      "\n" + super.code
  }

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
        case "SystemUser" => Seq("Identity")
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
        val argPartsPattern = """(.+): (.+?)(?:( = (.+))|$)""".r
        val Some(results) = entityCode match {
          case caseClassPattern(n, a) => Some((n, a))
          case _ => None
        }
        val name = results._1
        val args = results._2

        args.split(',').map { _.trim }.foldLeft(s"case class ${name}Filter(") ((agg, part) => {
          val parsedArgs = part match {
            case argPartsPattern(argName, argType, _, _) => Some((argName, argType))
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
        // для столбца role из SystemUserTable указываем, что нужно использовать тип Role
        // аналогичный тип будет использован и в entity SystemUser
        if (table.TableClass.name.toString == "SystemUserTable" && column.name.toString == "role") parseType("Role")
        // аналогично для rent_status.status
        else if (table.TableClass.name.toString == "RentStatusTable" && column.name.toString == "status") parseType("models.entities.RentStatus.RentStatus")
        else if (table.TableClass.name.toString == "OperationTable" && column.name.toString == "accountType") parseType("models.entities.AccountType.AccountType")
        else super.rawType.toString
      }
    }
  }
}
