package models.generated
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = play.api.db.slick.Config.driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._
  import models.entities.Role._
  import scala.slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import scala.slick.jdbc.{GetResult => GR}
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = Account.ddl ++ Car.ddl ++ CarClass.ddl ++ Checkpoint.ddl ++ Driver.ddl ++ Fine.ddl ++ Payment.ddl ++ Rent.ddl ++ RentStatus.ddl ++ Repair.ddl
  
  /** Entity class storing rows of table Account
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param login Database column login DBType(varchar), Length(254,true)
   *  @param passwordHash Database column password_hash DBType(varchar), Length(1000,true)
   *  @param lastName Database column last_name DBType(varchar), Length(254,true), Default(None)
   *  @param firstName Database column first_name DBType(varchar), Length(254,true), Default(None)
   *  @param middleName Database column middle_name DBType(varchar), Length(254,true), Default(None)
   *  @param role Database column role DBType(varchar), Length(254,true)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None) */
  case class AccountRow(id: Int, login: String, passwordHash: String, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, role: Role, creationDate: Option[java.sql.Date] = None, editDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None)
  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
  implicit def GetResultAccountRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Role], e4: GR[Option[java.sql.Date]], e5: GR[Option[Int]]): GR[AccountRow] = GR{
    prs => import prs._
    AccountRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<[Role], <<?[java.sql.Date], <<?[java.sql.Date], <<?[Int], <<?[Int]))
  }
  /** Table description of table account. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends Table[AccountRow](_tableTag, "account") {
    def * = (id, login, passwordHash, lastName, firstName, middleName, role, creationDate, editDate, creatorId, editorId) <> (AccountRow.tupled, AccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, login.?, passwordHash.?, lastName, firstName, middleName, role.?, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8, _9, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column login DBType(varchar), Length(254,true) */
    val login = column[String]("login", O.Length(254,varying=true))
    /** Database column password_hash DBType(varchar), Length(1000,true) */
    val passwordHash = column[String]("password_hash", O.Length(1000,varying=true))
    /** Database column last_name DBType(varchar), Length(254,true), Default(None) */
    val lastName = column[Option[String]]("last_name", O.Length(254,varying=true), O.Default(None))
    /** Database column first_name DBType(varchar), Length(254,true), Default(None) */
    val firstName = column[Option[String]]("first_name", O.Length(254,varying=true), O.Default(None))
    /** Database column middle_name DBType(varchar), Length(254,true), Default(None) */
    val middleName = column[Option[String]]("middle_name", O.Length(254,varying=true), O.Default(None))
    /** Database column role DBType(varchar), Length(254,true) */
    val role = column[Role]("role", O.Length(254,varying=true))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    
    /** Foreign key referencing Account (database name account_creator_id_fkey) */
    lazy val creatorFk = foreignKey("account_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name account_editor_id_fkey) */
    lazy val editorFk = foreignKey("account_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (login) (database name idx_login_uq) */
    val index1 = index("idx_login_uq", login, unique=true)
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))
  
  /** Entity class storing rows of table Car
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param regNumber Database column reg_number DBType(varchar), Length(12,true)
   *  @param make Database column make DBType(varchar), Length(255,true)
   *  @param model Database column model DBType(varchar), Length(255,true)
   *  @param mileage Database column mileage DBType(numeric)
   *  @param service Database column service DBType(numeric), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param classId Database column class_id DBType(int4) */
  case class CarRow(id: Int, regNumber: String, make: String, model: String, mileage: scala.math.BigDecimal, service: Option[scala.math.BigDecimal] = None, comment: Option[String] = None, creationDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, classId: Int)
  /** GetResult implicit for fetching CarRow objects using plain SQL queries */
  implicit def GetResultCarRow(implicit e0: GR[Int], e1: GR[String], e2: GR[scala.math.BigDecimal], e3: GR[Option[scala.math.BigDecimal]], e4: GR[Option[String]], e5: GR[Option[java.sql.Date]], e6: GR[Option[Int]]): GR[CarRow] = GR{
    prs => import prs._
    CarRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[scala.math.BigDecimal], <<?[scala.math.BigDecimal], <<?[String], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date], <<?[Int], <<[Int]))
  }
  /** Table description of table car. Objects of this class serve as prototypes for rows in queries. */
  class Car(_tableTag: Tag) extends Table[CarRow](_tableTag, "car") {
    def * = (id, regNumber, make, model, mileage, service, comment, creationDate, creatorId, editDate, editorId, classId) <> (CarRow.tupled, CarRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, regNumber.?, make.?, model.?, mileage.?, service, comment, creationDate, creatorId, editDate, editorId, classId.?).shaped.<>({r=>import r._; _1.map(_=> CarRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column reg_number DBType(varchar), Length(12,true) */
    val regNumber = column[String]("reg_number", O.Length(12,varying=true))
    /** Database column make DBType(varchar), Length(255,true) */
    val make = column[String]("make", O.Length(255,varying=true))
    /** Database column model DBType(varchar), Length(255,true) */
    val model = column[String]("model", O.Length(255,varying=true))
    /** Database column mileage DBType(numeric) */
    val mileage = column[scala.math.BigDecimal]("mileage")
    /** Database column service DBType(numeric), Default(None) */
    val service = column[Option[scala.math.BigDecimal]]("service", O.Default(None))
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column class_id DBType(int4) */
    val classId = column[Int]("class_id")
    
    /** Foreign key referencing Account (database name car_creator_id_fkey) */
    lazy val creatorFk = foreignKey("car_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name car_editor_id_fkey) */
    lazy val editorFk = foreignKey("car_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing CarClass (database name car_class_id_fkey) */
    lazy val classFk = foreignKey("car_class_id_fkey", classId, CarClass)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (regNumber) (database name unique_reg_number) */
    val index1 = index("unique_reg_number", regNumber, unique=true)
  }
  /** Collection-like TableQuery object for table Car */
  lazy val Car = new TableQuery(tag => new Car(tag))
  
  /** Entity class storing rows of table CarClass
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param name Database column name DBType(varchar), Length(255,true)
   *  @param rate Database column rate DBType(numeric)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
  case class CarClassRow(id: Int, name: String, rate: scala.math.BigDecimal, creationDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, comment: Option[String] = None)
  /** GetResult implicit for fetching CarClassRow objects using plain SQL queries */
  implicit def GetResultCarClassRow(implicit e0: GR[Int], e1: GR[String], e2: GR[scala.math.BigDecimal], e3: GR[Option[java.sql.Date]], e4: GR[Option[Int]], e5: GR[Option[String]]): GR[CarClassRow] = GR{
    prs => import prs._
    CarClassRow.tupled((<<[Int], <<[String], <<[scala.math.BigDecimal], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[String]))
  }
  /** Table description of table car_class. Objects of this class serve as prototypes for rows in queries. */
  class CarClass(_tableTag: Tag) extends Table[CarClassRow](_tableTag, "car_class") {
    def * = (id, name, rate, creationDate, creatorId, editDate, editorId, comment) <> (CarClassRow.tupled, CarClassRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, rate.?, creationDate, creatorId, editDate, editorId, comment).shaped.<>({r=>import r._; _1.map(_=> CarClassRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name DBType(varchar), Length(255,true) */
    val name = column[String]("name", O.Length(255,varying=true))
    /** Database column rate DBType(numeric) */
    val rate = column[scala.math.BigDecimal]("rate")
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    
    /** Foreign key referencing Account (database name car_class_creator_id_fkey) */
    lazy val creatorFk = foreignKey("car_class_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name car_class_editor_id_fkey) */
    lazy val editorFk = foreignKey("car_class_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (name) (database name unique_name) */
    val index1 = index("unique_name", name, unique=true)
  }
  /** Collection-like TableQuery object for table CarClass */
  lazy val CarClass = new TableQuery(tag => new CarClass(tag))
  
  /** Entity class storing rows of table Checkpoint
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param pointDate Database column point_date DBType(date)
   *  @param days Database column days DBType(int4)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
  case class CheckpointRow(id: Int, pointDate: java.sql.Date, days: Int, creationDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, comment: Option[String] = None)
  /** GetResult implicit for fetching CheckpointRow objects using plain SQL queries */
  implicit def GetResultCheckpointRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Option[java.sql.Date]], e3: GR[Option[Int]], e4: GR[Option[String]]): GR[CheckpointRow] = GR{
    prs => import prs._
    CheckpointRow.tupled((<<[Int], <<[java.sql.Date], <<[Int], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[String]))
  }
  /** Table description of table checkpoint. Objects of this class serve as prototypes for rows in queries. */
  class Checkpoint(_tableTag: Tag) extends Table[CheckpointRow](_tableTag, "checkpoint") {
    def * = (id, pointDate, days, creationDate, creatorId, editDate, editorId, comment) <> (CheckpointRow.tupled, CheckpointRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, pointDate.?, days.?, creationDate, creatorId, editDate, editorId, comment).shaped.<>({r=>import r._; _1.map(_=> CheckpointRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column point_date DBType(date) */
    val pointDate = column[java.sql.Date]("point_date")
    /** Database column days DBType(int4) */
    val days = column[Int]("days")
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    
    /** Foreign key referencing Account (database name checkpoint_creator_id_fkey) */
    lazy val creatorFk = foreignKey("checkpoint_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name checkpoint_editor_id_fkey) */
    lazy val editorFk = foreignKey("checkpoint_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Index over (pointDate) (database name point_date_index) */
    val index1 = index("point_date_index", pointDate)
  }
  /** Collection-like TableQuery object for table Checkpoint */
  lazy val Checkpoint = new TableQuery(tag => new Checkpoint(tag))
  
  /** Entity class storing rows of table Driver
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param pass Database column pass DBType(varchar), Length(254,true)
   *  @param license Database column license DBType(varchar), Length(254,true)
   *  @param lastName Database column last_name DBType(varchar), Length(254,true), Default(None)
   *  @param firstName Database column first_name DBType(varchar), Length(254,true), Default(None)
   *  @param middleName Database column middle_name DBType(varchar), Length(254,true), Default(None)
   *  @param phone Database column phone DBType(varchar), Length(254,true)
   *  @param secPhone Database column sec_phone DBType(varchar), Length(254,true)
   *  @param comment Database column comment DBType(varchar), Length(254,true), Default(None)
   *  @param address Database column address DBType(varchar), Length(254,true)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None) */
  case class DriverRow(id: Int, pass: String, license: String, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, phone: String, secPhone: String, comment: Option[String] = None, address: String, creationDate: Option[java.sql.Date] = None, editDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None)
  /** GetResult implicit for fetching DriverRow objects using plain SQL queries */
  implicit def GetResultDriverRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[java.sql.Date]], e4: GR[Option[Int]]): GR[DriverRow] = GR{
    prs => import prs._
    DriverRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<[String], <<[String], <<?[String], <<[String], <<?[java.sql.Date], <<?[java.sql.Date], <<?[Int], <<?[Int]))
  }
  /** Table description of table driver. Objects of this class serve as prototypes for rows in queries. */
  class Driver(_tableTag: Tag) extends Table[DriverRow](_tableTag, "driver") {
    def * = (id, pass, license, lastName, firstName, middleName, phone, secPhone, comment, address, creationDate, editDate, creatorId, editorId) <> (DriverRow.tupled, DriverRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, pass.?, license.?, lastName, firstName, middleName, phone.?, secPhone.?, comment, address.?, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> DriverRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8.get, _9, _10.get, _11, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column pass DBType(varchar), Length(254,true) */
    val pass = column[String]("pass", O.Length(254,varying=true))
    /** Database column license DBType(varchar), Length(254,true) */
    val license = column[String]("license", O.Length(254,varying=true))
    /** Database column last_name DBType(varchar), Length(254,true), Default(None) */
    val lastName = column[Option[String]]("last_name", O.Length(254,varying=true), O.Default(None))
    /** Database column first_name DBType(varchar), Length(254,true), Default(None) */
    val firstName = column[Option[String]]("first_name", O.Length(254,varying=true), O.Default(None))
    /** Database column middle_name DBType(varchar), Length(254,true), Default(None) */
    val middleName = column[Option[String]]("middle_name", O.Length(254,varying=true), O.Default(None))
    /** Database column phone DBType(varchar), Length(254,true) */
    val phone = column[String]("phone", O.Length(254,varying=true))
    /** Database column sec_phone DBType(varchar), Length(254,true) */
    val secPhone = column[String]("sec_phone", O.Length(254,varying=true))
    /** Database column comment DBType(varchar), Length(254,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(254,varying=true), O.Default(None))
    /** Database column address DBType(varchar), Length(254,true) */
    val address = column[String]("address", O.Length(254,varying=true))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    
    /** Foreign key referencing Account (database name driver_creator_id_fkey) */
    lazy val creatorFk = foreignKey("driver_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name driver_editor_id_fkey) */
    lazy val editorFk = foreignKey("driver_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (license) (database name idx_license_uq) */
    val index1 = index("idx_license_uq", license, unique=true)
    /** Uniqueness Index over (pass) (database name idx_pass_uq) */
    val index2 = index("idx_pass_uq", pass, unique=true)
  }
  /** Collection-like TableQuery object for table Driver */
  lazy val Driver = new TableQuery(tag => new Driver(tag))
  
  /** Entity class storing rows of table Fine
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param fineDate Database column fine_date DBType(date)
   *  @param cost Database column cost DBType(numeric)
   *  @param description Database column description DBType(varchar), Length(2147483647,true), Default(None)
   *  @param rentId Database column rent_id DBType(int4)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None) */
  case class FineRow(id: Int, fineDate: java.sql.Date, cost: scala.math.BigDecimal, description: Option[String] = None, rentId: Int, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching FineRow objects using plain SQL queries */
  implicit def GetResultFineRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[scala.math.BigDecimal], e3: GR[Option[String]], e4: GR[Option[Int]], e5: GR[Option[java.sql.Date]]): GR[FineRow] = GR{
    prs => import prs._
    FineRow.tupled((<<[Int], <<[java.sql.Date], <<[scala.math.BigDecimal], <<?[String], <<[Int], <<?[String], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date]))
  }
  /** Table description of table fine. Objects of this class serve as prototypes for rows in queries. */
  class Fine(_tableTag: Tag) extends Table[FineRow](_tableTag, "fine") {
    def * = (id, fineDate, cost, description, rentId, comment, creatorId, creationDate, editorId, editDate) <> (FineRow.tupled, FineRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, fineDate.?, cost.?, description, rentId.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> FineRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column fine_date DBType(date) */
    val fineDate = column[java.sql.Date]("fine_date")
    /** Database column cost DBType(numeric) */
    val cost = column[scala.math.BigDecimal]("cost")
    /** Database column description DBType(varchar), Length(2147483647,true), Default(None) */
    val description = column[Option[String]]("description", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    
    /** Foreign key referencing Account (database name fine_creator_id_fkey) */
    lazy val creatorFk = foreignKey("fine_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name fine_editor_id_fkey) */
    lazy val editorFk = foreignKey("fine_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Rent (database name fine_rent_id_fkey) */
    lazy val rentFk = foreignKey("fine_rent_id_fkey", rentId, Rent)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Fine */
  lazy val Fine = new TableQuery(tag => new Fine(tag))
  
  /** Entity class storing rows of table Payment
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param payDate Database column pay_date DBType(date)
   *  @param amount Database column amount DBType(numeric)
   *  @param target Database column target DBType(varchar), Length(255,true)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param rentId Database column rent_id DBType(int4) */
  case class PaymentRow(id: Int, payDate: java.sql.Date, amount: scala.math.BigDecimal, target: String, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, rentId: Int)
  /** GetResult implicit for fetching PaymentRow objects using plain SQL queries */
  implicit def GetResultPaymentRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[scala.math.BigDecimal], e3: GR[String], e4: GR[Option[String]], e5: GR[Option[Int]], e6: GR[Option[java.sql.Date]]): GR[PaymentRow] = GR{
    prs => import prs._
    PaymentRow.tupled((<<[Int], <<[java.sql.Date], <<[scala.math.BigDecimal], <<[String], <<?[String], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date], <<[Int]))
  }
  /** Table description of table payment. Objects of this class serve as prototypes for rows in queries. */
  class Payment(_tableTag: Tag) extends Table[PaymentRow](_tableTag, "payment") {
    def * = (id, payDate, amount, target, comment, creatorId, creationDate, editorId, editDate, rentId) <> (PaymentRow.tupled, PaymentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, payDate.?, amount.?, target.?, comment, creatorId, creationDate, editorId, editDate, rentId.?).shaped.<>({r=>import r._; _1.map(_=> PaymentRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column pay_date DBType(date) */
    val payDate = column[java.sql.Date]("pay_date")
    /** Database column amount DBType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column target DBType(varchar), Length(255,true) */
    val target = column[String]("target", O.Length(255,varying=true))
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    
    /** Foreign key referencing Account (database name payment_creator_id_fkey) */
    lazy val creatorFk = foreignKey("payment_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name payment_editor_id_fkey) */
    lazy val editorFk = foreignKey("payment_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Rent (database name payment_rent_id_fkey) */
    lazy val rentFk = foreignKey("payment_rent_id_fkey", rentId, Rent)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Payment */
  lazy val Payment = new TableQuery(tag => new Payment(tag))
  
  /** Entity class storing rows of table Rent
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param driverId Database column driver_id DBType(int4)
   *  @param carId Database column car_id DBType(int4)
   *  @param deposit Database column deposit DBType(numeric)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None) */
  case class RentRow(id: Int, driverId: Int, carId: Int, deposit: scala.math.BigDecimal, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching RentRow objects using plain SQL queries */
  implicit def GetResultRentRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal], e2: GR[Option[String]], e3: GR[Option[Int]], e4: GR[Option[java.sql.Date]]): GR[RentRow] = GR{
    prs => import prs._
    RentRow.tupled((<<[Int], <<[Int], <<[Int], <<[scala.math.BigDecimal], <<?[String], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date]))
  }
  /** Table description of table rent. Objects of this class serve as prototypes for rows in queries. */
  class Rent(_tableTag: Tag) extends Table[RentRow](_tableTag, "rent") {
    def * = (id, driverId, carId, deposit, comment, creatorId, creationDate, editorId, editDate) <> (RentRow.tupled, RentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, driverId.?, carId.?, deposit.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> RentRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column driver_id DBType(int4) */
    val driverId = column[Int]("driver_id")
    /** Database column car_id DBType(int4) */
    val carId = column[Int]("car_id")
    /** Database column deposit DBType(numeric) */
    val deposit = column[scala.math.BigDecimal]("deposit")
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    
    /** Foreign key referencing Account (database name rent_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name rent_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Car (database name rent_car_id_fkey) */
    lazy val carFk = foreignKey("rent_car_id_fkey", carId, Car)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Driver (database name rent_driver_id_fkey) */
    lazy val driverFk = foreignKey("rent_driver_id_fkey", driverId, Driver)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Rent */
  lazy val Rent = new TableQuery(tag => new Rent(tag))
  
  /** Entity class storing rows of table RentStatus
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeDate Database column change_date DBType(date)
   *  @param status Database column status DBType(varchar), Length(255,true)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None) */
  case class RentStatusRow(id: Int, changeDate: java.sql.Date, status: String, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching RentStatusRow objects using plain SQL queries */
  implicit def GetResultRentStatusRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[String], e3: GR[Option[String]], e4: GR[Option[Int]], e5: GR[Option[java.sql.Date]]): GR[RentStatusRow] = GR{
    prs => import prs._
    RentStatusRow.tupled((<<[Int], <<[java.sql.Date], <<[String], <<?[String], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date]))
  }
  /** Table description of table rent_status. Objects of this class serve as prototypes for rows in queries. */
  class RentStatus(_tableTag: Tag) extends Table[RentStatusRow](_tableTag, "rent_status") {
    def * = (id, changeDate, status, comment, creatorId, creationDate, editorId, editDate) <> (RentStatusRow.tupled, RentStatusRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, changeDate.?, status.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> RentStatusRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_date DBType(date) */
    val changeDate = column[java.sql.Date]("change_date")
    /** Database column status DBType(varchar), Length(255,true) */
    val status = column[String]("status", O.Length(255,varying=true))
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    
    /** Foreign key referencing Account (database name rent_status_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_status_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name rent_status_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_status_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Index over (changeDate) (database name change_date_index) */
    val index1 = index("change_date_index", changeDate)
    /** Index over (status) (database name status_index) */
    val index2 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table RentStatus */
  lazy val RentStatus = new TableQuery(tag => new RentStatus(tag))
  
  /** Entity class storing rows of table Repair
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param repairDate Database column repair_date DBType(date)
   *  @param cost Database column cost DBType(numeric)
   *  @param description Database column description DBType(varchar), Length(2147483647,true), Default(None)
   *  @param rentId Database column rent_id DBType(int4)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None) */
  case class RepairRow(id: Int, repairDate: java.sql.Date, cost: scala.math.BigDecimal, description: Option[String] = None, rentId: Int, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching RepairRow objects using plain SQL queries */
  implicit def GetResultRepairRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[scala.math.BigDecimal], e3: GR[Option[String]], e4: GR[Option[Int]], e5: GR[Option[java.sql.Date]]): GR[RepairRow] = GR{
    prs => import prs._
    RepairRow.tupled((<<[Int], <<[java.sql.Date], <<[scala.math.BigDecimal], <<?[String], <<[Int], <<?[String], <<?[Int], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date]))
  }
  /** Table description of table repair. Objects of this class serve as prototypes for rows in queries. */
  class Repair(_tableTag: Tag) extends Table[RepairRow](_tableTag, "repair") {
    def * = (id, repairDate, cost, description, rentId, comment, creatorId, creationDate, editorId, editDate) <> (RepairRow.tupled, RepairRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, repairDate.?, cost.?, description, rentId.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> RepairRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column repair_date DBType(date) */
    val repairDate = column[java.sql.Date]("repair_date")
    /** Database column cost DBType(numeric) */
    val cost = column[scala.math.BigDecimal]("cost")
    /** Database column description DBType(varchar), Length(2147483647,true), Default(None) */
    val description = column[Option[String]]("description", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    
    /** Foreign key referencing Account (database name repair_creator_id_fkey) */
    lazy val creatorFk = foreignKey("repair_creator_id_fkey", creatorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name repair_editor_id_fkey) */
    lazy val editorFk = foreignKey("repair_editor_id_fkey", editorId, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Rent (database name repair_rent_id_fkey) */
    lazy val rentFk = foreignKey("repair_rent_id_fkey", rentId, Rent)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Repair */
  lazy val Repair = new TableQuery(tag => new Repair(tag))
}