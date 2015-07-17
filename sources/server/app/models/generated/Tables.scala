package models.generated.draft
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import models.entities._
  import models.entities.Role.Role
  import db.MappedColumnTypes._
  import com.mohiva.play.silhouette.api.Identity
  import utils.DateUtils
  import slick.model.ForeignKeyAction

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(CarTable.schema, DriverTable.schema, OperationTable.schema, RefundTable.schema, RentStatusTable.schema, RentTable.schema, SystemUserTable.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table CarTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param regNumber Database column reg_number SqlType(varchar), Length(12,true)
   *  @param make Database column make SqlType(varchar), Length(255,true)
   *  @param model Database column model SqlType(varchar), Length(255,true)
   *  @param rate Database column rate SqlType(numeric)
   *  @param mileage Database column mileage SqlType(numeric)
   *  @param service Database column service SqlType(numeric), Default(None)
   *  @param color Database column color SqlType(varchar), Length(255,true), Default(None)
   *  @param year Database column year SqlType(int4), Default(None)
   *  @param comment Database column comment SqlType(varchar), Length(5000,true), Default(None)
   *  @param creationDate Database column creation_date SqlType(timestamptz), Default(None)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None) */
  case class Car(id: Int, regNumber: String, make: String, model: String, rate: scala.math.BigDecimal, mileage: scala.math.BigDecimal, service: Option[scala.math.BigDecimal] = None, color: Option[String] = None, year: Option[Int] = None, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None) extends Entity[Car]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class CarFilter(id: Option[Int] = None, regNumber: Option[String] = None, make: Option[String] = None, model: Option[String] = None, rate: Option[scala.math.BigDecimal] = None, mileage: Option[scala.math.BigDecimal] = None, service: Option[scala.math.BigDecimal] = None, color: Option[String] = None, year: Option[Int] = None, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None)

  /** Table description of table car. Objects of this class serve as prototypes for rows in queries. */
  class CarTable(_tableTag: Tag) extends Table[Car](_tableTag, "car") {
    def * = (id, regNumber, make, model, rate, mileage, service, color, year, comment, creationDate, creatorId, editDate, editorId) <> (Car.tupled, Car.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(regNumber), Rep.Some(make), Rep.Some(model), Rep.Some(rate), Rep.Some(mileage), service, color, year, comment, creationDate, creatorId, editDate, editorId).shaped.<>({r=>import r._; _1.map(_=> Car.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column reg_number SqlType(varchar), Length(12,true) */
    val regNumber = column[String]("reg_number", O.Length(12,varying=true))
    /** Database column make SqlType(varchar), Length(255,true) */
    val make = column[String]("make", O.Length(255,varying=true))
    /** Database column model SqlType(varchar), Length(255,true) */
    val model = column[String]("model", O.Length(255,varying=true))
    /** Database column rate SqlType(numeric) */
    val rate = column[scala.math.BigDecimal]("rate")
    /** Database column mileage SqlType(numeric) */
    val mileage = column[scala.math.BigDecimal]("mileage")
    /** Database column service SqlType(numeric), Default(None) */
    val service = column[Option[scala.math.BigDecimal]]("service", O.Default(None))
    /** Database column color SqlType(varchar), Length(255,true), Default(None) */
    val color = column[Option[String]]("color", O.Length(255,varying=true), O.Default(None))
    /** Database column year SqlType(int4), Default(None) */
    val year = column[Option[Int]]("year", O.Default(None))
    /** Database column comment SqlType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creation_date SqlType(timestamptz), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))

    /** Foreign key referencing SystemUserTable (database name car_creator_id_fkey) */
    lazy val creatorFk = foreignKey("car_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name car_editor_id_fkey) */
    lazy val editorFk = foreignKey("car_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (regNumber) (database name unique_reg_number) */
    val index1 = index("unique_reg_number", regNumber, unique=true)
  }
  /** Collection-like TableQuery object for table CarTable */
  lazy val CarTable = new TableQuery(tag => new CarTable(tag))

  /** Entity class storing rows of table DriverTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param pass Database column pass SqlType(varchar), Length(254,true)
   *  @param license Database column license SqlType(varchar), Length(254,true)
   *  @param lastName Database column last_name SqlType(varchar), Length(254,true)
   *  @param firstName Database column first_name SqlType(varchar), Length(254,true)
   *  @param middleName Database column middle_name SqlType(varchar), Length(254,true), Default(None)
   *  @param phone Database column phone SqlType(varchar), Length(254,true)
   *  @param secPhone Database column sec_phone SqlType(varchar), Length(254,true)
   *  @param comment Database column comment SqlType(varchar), Length(254,true), Default(None)
   *  @param address Database column address SqlType(varchar), Length(254,true)
   *  @param creationDate Database column creation_date SqlType(timestamptz), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None) */
  case class Driver(id: Int, pass: String, license: String, lastName: String, firstName: String, middleName: Option[String] = None, phone: String, secPhone: String, comment: Option[String] = None, address: String, creationDate: Option[java.sql.Timestamp] = None, editDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None) extends Entity[Driver]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class DriverFilter(id: Option[Int] = None, pass: Option[String] = None, license: Option[String] = None, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, phone: Option[String] = None, secPhone: Option[String] = None, comment: Option[String] = None, address: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, editDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None)

  /** Table description of table driver. Objects of this class serve as prototypes for rows in queries. */
  class DriverTable(_tableTag: Tag) extends Table[Driver](_tableTag, "driver") {
    def * = (id, pass, license, lastName, firstName, middleName, phone, secPhone, comment, address, creationDate, editDate, creatorId, editorId) <> (Driver.tupled, Driver.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(pass), Rep.Some(license), Rep.Some(lastName), Rep.Some(firstName), middleName, Rep.Some(phone), Rep.Some(secPhone), comment, Rep.Some(address), creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> Driver.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9, _10.get, _11, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column pass SqlType(varchar), Length(254,true) */
    val pass = column[String]("pass", O.Length(254,varying=true))
    /** Database column license SqlType(varchar), Length(254,true) */
    val license = column[String]("license", O.Length(254,varying=true))
    /** Database column last_name SqlType(varchar), Length(254,true) */
    val lastName = column[String]("last_name", O.Length(254,varying=true))
    /** Database column first_name SqlType(varchar), Length(254,true) */
    val firstName = column[String]("first_name", O.Length(254,varying=true))
    /** Database column middle_name SqlType(varchar), Length(254,true), Default(None) */
    val middleName = column[Option[String]]("middle_name", O.Length(254,varying=true), O.Default(None))
    /** Database column phone SqlType(varchar), Length(254,true) */
    val phone = column[String]("phone", O.Length(254,varying=true))
    /** Database column sec_phone SqlType(varchar), Length(254,true) */
    val secPhone = column[String]("sec_phone", O.Length(254,varying=true))
    /** Database column comment SqlType(varchar), Length(254,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(254,varying=true), O.Default(None))
    /** Database column address SqlType(varchar), Length(254,true) */
    val address = column[String]("address", O.Length(254,varying=true))
    /** Database column creation_date SqlType(timestamptz), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))

    /** Foreign key referencing SystemUserTable (database name driver_creator_id_fkey) */
    lazy val creatorFk = foreignKey("driver_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name driver_editor_id_fkey) */
    lazy val editorFk = foreignKey("driver_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (license) (database name idx_license_uq) */
    val index1 = index("idx_license_uq", license, unique=true)
    /** Uniqueness Index over (pass) (database name idx_pass_uq) */
    val index2 = index("idx_pass_uq", pass, unique=true)
  }
  /** Collection-like TableQuery object for table DriverTable */
  lazy val DriverTable = new TableQuery(tag => new DriverTable(tag))

  /** Entity class storing rows of table OperationTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param rentId Database column rent_id SqlType(int4)
   *  @param amount Database column amount SqlType(numeric)
   *  @param changeTime Database column change_time SqlType(timestamptz)
   *  @param accountType Database column account_type SqlType(varchar), Length(100,true)
   *  @param presence Database column presence SqlType(bool), Default(true)
   *  @param comment Database column comment SqlType(varchar), Length(5000,true), Default(None)
   *  @param creationDate Database column creation_date SqlType(timestamptz), Default(None)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None) */
  case class Operation(id: Int, rentId: Int, amount: scala.math.BigDecimal, changeTime: java.sql.Timestamp, accountType: models.entities.AccountType.AccountType, presence: Boolean = true, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None) extends Entity[Operation]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class OperationFilter(id: Option[Int] = None, rentId: Option[Int] = None, amount: Option[scala.math.BigDecimal] = None, changeTime: Option[java.sql.Timestamp] = None, accountType: Option[models.entities.AccountType.AccountType] = None, presence: Option[Boolean] = None, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None)

  /** Table description of table operation. Objects of this class serve as prototypes for rows in queries. */
  class OperationTable(_tableTag: Tag) extends Table[Operation](_tableTag, "operation") {
    def * = (id, rentId, amount, changeTime, accountType, presence, comment, creationDate, creatorId, editDate, editorId) <> (Operation.tupled, Operation.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(rentId), Rep.Some(amount), Rep.Some(changeTime), Rep.Some(accountType), Rep.Some(presence), comment, creationDate, creatorId, editDate, editorId).shaped.<>({r=>import r._; _1.map(_=> Operation.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column rent_id SqlType(int4) */
    val rentId = column[Int]("rent_id")
    /** Database column amount SqlType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column change_time SqlType(timestamptz) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column account_type SqlType(varchar), Length(100,true) */
    val accountType = column[models.entities.AccountType.AccountType]("account_type", O.Length(100,varying=true))
    /** Database column presence SqlType(bool), Default(true) */
    val presence = column[Boolean]("presence", O.Default(true))
    /** Database column comment SqlType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creation_date SqlType(timestamptz), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))

    /** Foreign key referencing RentTable (database name operation_rent_id_fkey) */
    lazy val rentFk = foreignKey("operation_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name operation_creator_id_fkey) */
    lazy val creatorFk = foreignKey("operation_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name operation_editor_id_fkey) */
    lazy val editorFk = foreignKey("operation_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table OperationTable */
  lazy val OperationTable = new TableQuery(tag => new OperationTable(tag))

  /** Entity class storing rows of table RefundTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param amount Database column amount SqlType(numeric)
   *  @param changeTime Database column change_time SqlType(timestamptz)
   *  @param creationDate Database column creation_date SqlType(timestamptz)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None)
   *  @param comment Database column comment SqlType(varchar), Length(1000,true), Default(None)
   *  @param rentId Database column rent_id SqlType(int4) */
  case class Refund(id: Int, amount: scala.math.BigDecimal, changeTime: java.sql.Timestamp, creationDate: java.sql.Timestamp, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, comment: Option[String] = None, rentId: Int) extends Entity[Refund]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class RefundFilter(id: Option[Int] = None, amount: Option[scala.math.BigDecimal] = None, changeTime: Option[java.sql.Timestamp] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, comment: Option[String] = None, rentId: Option[Int] = None)

  /** Table description of table refund. Objects of this class serve as prototypes for rows in queries. */
  class RefundTable(_tableTag: Tag) extends Table[Refund](_tableTag, "refund") {
    def * = (id, amount, changeTime, creationDate, creatorId, editDate, editorId, comment, rentId) <> (Refund.tupled, Refund.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(amount), Rep.Some(changeTime), Rep.Some(creationDate), creatorId, editDate, editorId, comment, Rep.Some(rentId)).shaped.<>({r=>import r._; _1.map(_=> Refund.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column amount SqlType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column change_time SqlType(timestamptz) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column creation_date SqlType(timestamptz) */
    val creationDate = column[java.sql.Timestamp]("creation_date")
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column comment SqlType(varchar), Length(1000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(1000,varying=true), O.Default(None))
    /** Database column rent_id SqlType(int4) */
    val rentId = column[Int]("rent_id")

    /** Foreign key referencing RentTable (database name refund_rent_id_fkey) */
    lazy val rentFk = foreignKey("refund_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name refund_creator_id_fkey) */
    lazy val creatorFk = foreignKey("refund_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name refund_editor_id_fkey) */
    lazy val editorFk = foreignKey("refund_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table RefundTable */
  lazy val RefundTable = new TableQuery(tag => new RefundTable(tag))

  /** Entity class storing rows of table RentStatusTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param changeTime Database column change_time SqlType(timestamptz)
   *  @param status Database column status SqlType(varchar), Length(255,true)
   *  @param comment Database column comment SqlType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param creationDate Database column creation_date SqlType(timestamptz), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None)
   *  @param rentId Database column rent_id SqlType(int4) */
  case class RentStatus(id: Int, changeTime: java.sql.Timestamp, status: models.entities.RentStatus.RentStatus, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, rentId: Int) extends Entity[RentStatus]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class RentStatusFilter(id: Option[Int] = None, changeTime: Option[java.sql.Timestamp] = None, status: Option[models.entities.RentStatus.RentStatus] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, rentId: Option[Int] = None)

  /** Table description of table rent_status. Objects of this class serve as prototypes for rows in queries. */
  class RentStatusTable(_tableTag: Tag) extends Table[RentStatus](_tableTag, "rent_status") {
    def * = (id, changeTime, status, comment, creatorId, creationDate, editorId, editDate, rentId) <> (RentStatus.tupled, RentStatus.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(changeTime), Rep.Some(status), comment, creatorId, creationDate, editorId, editDate, Rep.Some(rentId)).shaped.<>({r=>import r._; _1.map(_=> RentStatus.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_time SqlType(timestamptz) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column status SqlType(varchar), Length(255,true) */
    val status = column[models.entities.RentStatus.RentStatus]("status", O.Length(255,varying=true))
    /** Database column comment SqlType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date SqlType(timestamptz), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column rent_id SqlType(int4) */
    val rentId = column[Int]("rent_id")

    /** Foreign key referencing RentTable (database name rent_status_rent_id_fkey) */
    lazy val rentFk = foreignKey("rent_status_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing SystemUserTable (database name rent_status_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_status_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name rent_status_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_status_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (changeTime) (database name change_date_index) */
    val index1 = index("change_date_index", changeTime)
    /** Index over (status) (database name status_index) */
    val index2 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table RentStatusTable */
  lazy val RentStatusTable = new TableQuery(tag => new RentStatusTable(tag))

  /** Entity class storing rows of table RentTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param driverId Database column driver_id SqlType(int4)
   *  @param carId Database column car_id SqlType(int4)
   *  @param deposit Database column deposit SqlType(numeric)
   *  @param comment Database column comment SqlType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param creationDate Database column creation_date SqlType(timestamptz), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None) */
  case class Rent(id: Int, driverId: Int, carId: Int, deposit: scala.math.BigDecimal, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None) extends Entity[Rent]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class RentFilter(id: Option[Int] = None, driverId: Option[Int] = None, carId: Option[Int] = None, deposit: Option[scala.math.BigDecimal] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None)

  /** Table description of table rent. Objects of this class serve as prototypes for rows in queries. */
  class RentTable(_tableTag: Tag) extends Table[Rent](_tableTag, "rent") {
    def * = (id, driverId, carId, deposit, comment, creatorId, creationDate, editorId, editDate) <> (Rent.tupled, Rent.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(driverId), Rep.Some(carId), Rep.Some(deposit), comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Rent.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column driver_id SqlType(int4) */
    val driverId = column[Int]("driver_id")
    /** Database column car_id SqlType(int4) */
    val carId = column[Int]("car_id")
    /** Database column deposit SqlType(numeric) */
    val deposit = column[scala.math.BigDecimal]("deposit")
    /** Database column comment SqlType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date SqlType(timestamptz), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))

    /** Foreign key referencing CarTable (database name rent_car_id_fkey) */
    lazy val carFk = foreignKey("rent_car_id_fkey", carId, CarTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing DriverTable (database name rent_driver_id_fkey) */
    lazy val driverFk = foreignKey("rent_driver_id_fkey", driverId, DriverTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name rent_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name rent_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table RentTable */
  lazy val RentTable = new TableQuery(tag => new RentTable(tag))

  /** Entity class storing rows of table SystemUserTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param login Database column login SqlType(varchar), Length(254,true)
   *  @param passwordHash Database column password_hash SqlType(varchar), Length(1000,true)
   *  @param lastName Database column last_name SqlType(varchar), Length(254,true), Default(None)
   *  @param firstName Database column first_name SqlType(varchar), Length(254,true), Default(None)
   *  @param middleName Database column middle_name SqlType(varchar), Length(254,true), Default(None)
   *  @param role Database column role SqlType(varchar), Length(254,true)
   *  @param comment Database column comment SqlType(varchar), Length(5000,true), Default(None)
   *  @param creationDate Database column creation_date SqlType(timestamptz), Default(None)
   *  @param editDate Database column edit_date SqlType(timestamptz), Default(None)
   *  @param creatorId Database column creator_id SqlType(int4), Default(None)
   *  @param editorId Database column editor_id SqlType(int4), Default(None) */
  case class SystemUser(id: Int, login: String, passwordHash: String, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, role: Role, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, editDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None) extends Entity[SystemUser] with Identity
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class SystemUserFilter(id: Option[Int] = None, login: Option[String] = None, passwordHash: Option[String] = None, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, role: Option[Role] = None, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, editDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None)

  /** Table description of table system_user. Objects of this class serve as prototypes for rows in queries. */
  class SystemUserTable(_tableTag: Tag) extends Table[SystemUser](_tableTag, "system_user") {
    def * = (id, login, passwordHash, lastName, firstName, middleName, role, comment, creationDate, editDate, creatorId, editorId) <> (SystemUser.tupled, SystemUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(passwordHash), lastName, firstName, middleName, Rep.Some(role), comment, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> SystemUser.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login SqlType(varchar), Length(254,true) */
    val login = column[String]("login", O.Length(254,varying=true))
    /** Database column password_hash SqlType(varchar), Length(1000,true) */
    val passwordHash = column[String]("password_hash", O.Length(1000,varying=true))
    /** Database column last_name SqlType(varchar), Length(254,true), Default(None) */
    val lastName = column[Option[String]]("last_name", O.Length(254,varying=true), O.Default(None))
    /** Database column first_name SqlType(varchar), Length(254,true), Default(None) */
    val firstName = column[Option[String]]("first_name", O.Length(254,varying=true), O.Default(None))
    /** Database column middle_name SqlType(varchar), Length(254,true), Default(None) */
    val middleName = column[Option[String]]("middle_name", O.Length(254,varying=true), O.Default(None))
    /** Database column role SqlType(varchar), Length(254,true) */
    val role = column[Role]("role", O.Length(254,varying=true))
    /** Database column comment SqlType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creation_date SqlType(timestamptz), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column edit_date SqlType(timestamptz), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column creator_id SqlType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id SqlType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))

    /** Foreign key referencing SystemUserTable (database name system_user_creator_id_fkey) */
    lazy val creatorFk = foreignKey("system_user_creator_id_fkey", creatorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name system_user_editor_id_fkey) */
    lazy val editorFk = foreignKey("system_user_editor_id_fkey", editorId, SystemUserTable)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (login) (database name idx_login_uq) */
    val index1 = index("idx_login_uq", login, unique=true)
  }
  /** Collection-like TableQuery object for table SystemUserTable */
  lazy val SystemUserTable = new TableQuery(tag => new SystemUserTable(tag))
}
