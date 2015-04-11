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
  import models.entities._
  import models.entities.Role._
  import com.mohiva.play.silhouette.api.Identity
  import scala.slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import scala.slick.jdbc.{GetResult => GR}
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = AccountTable.ddl ++ CarClassTable.ddl ++ CarTable.ddl ++ CheckpointTable.ddl ++ DriverTable.ddl ++ FineTable.ddl ++ PaymentTable.ddl ++ RentStatusTable.ddl ++ RentTable.ddl ++ RepairTable.ddl
  
  /** Entity class storing rows of table AccountTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param login Database column login DBType(varchar), Length(254,true)
   *  @param passwordHash Database column password_hash DBType(varchar), Length(1000,true)
   *  @param lastName Database column last_name DBType(varchar), Length(254,true), Default(None)
   *  @param firstName Database column first_name DBType(varchar), Length(254,true), Default(None)
   *  @param middleName Database column middle_name DBType(varchar), Length(254,true), Default(None)
   *  @param role Database column role DBType(varchar), Length(254,true)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None) */
  case class Account(id: Int, login: String, passwordHash: String, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, role: Role, comment: Option[String] = None, creationDate: Option[java.sql.Date] = None, editDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None) extends Entity with Identity
  /** GetResult implicit for fetching Account objects using plain SQL queries */
  
  /** Table description of table account. Objects of this class serve as prototypes for rows in queries. */
  class AccountTable(_tableTag: Tag) extends Table[Account](_tableTag, "account") {
    def * = (id, login, passwordHash, lastName, firstName, middleName, role, comment, creationDate, editDate, creatorId, editorId) <> (Account.tupled, Account.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, login.?, passwordHash.?, lastName, firstName, middleName, role.?, comment, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> Account.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    /** Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(2147483647,varying=true), O.Default(None))
    /** Database column creation_date DBType(date), Default(None) */
    val creationDate = column[Option[java.sql.Date]]("creation_date", O.Default(None))
    /** Database column edit_date DBType(date), Default(None) */
    val editDate = column[Option[java.sql.Date]]("edit_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    
    /** Foreign key referencing AccountTable (database name account_creator_id_fkey) */
    lazy val creatorFk = foreignKey("account_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name account_editor_id_fkey) */
    lazy val editorFk = foreignKey("account_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (login) (database name idx_login_uq) */
    val index1 = index("idx_login_uq", login, unique=true)
  }
  /** Collection-like TableQuery object for table AccountTable */
  lazy val AccountTable = new TableQuery(tag => new AccountTable(tag))
  
  /** Entity class storing rows of table CarClassTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param name Database column name DBType(varchar), Length(255,true)
   *  @param rate Database column rate DBType(numeric)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
  case class CarClass(id: Int, name: String, rate: scala.math.BigDecimal, creationDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, comment: Option[String] = None) extends Entity
  /** GetResult implicit for fetching CarClass objects using plain SQL queries */
  
  /** Table description of table car_class. Objects of this class serve as prototypes for rows in queries. */
  class CarClassTable(_tableTag: Tag) extends Table[CarClass](_tableTag, "car_class") {
    def * = (id, name, rate, creationDate, creatorId, editDate, editorId, comment) <> (CarClass.tupled, CarClass.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, rate.?, creationDate, creatorId, editDate, editorId, comment).shaped.<>({r=>import r._; _1.map(_=> CarClass.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name car_class_creator_id_fkey) */
    lazy val creatorFk = foreignKey("car_class_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name car_class_editor_id_fkey) */
    lazy val editorFk = foreignKey("car_class_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (name) (database name unique_name) */
    val index1 = index("unique_name", name, unique=true)
  }
  /** Collection-like TableQuery object for table CarClassTable */
  lazy val CarClassTable = new TableQuery(tag => new CarClassTable(tag))
  
  /** Entity class storing rows of table CarTable
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
  case class Car(id: Int, regNumber: String, make: String, model: String, mileage: scala.math.BigDecimal, service: Option[scala.math.BigDecimal] = None, comment: Option[String] = None, creationDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, classId: Int) extends Entity
  /** GetResult implicit for fetching Car objects using plain SQL queries */
  
  /** Table description of table car. Objects of this class serve as prototypes for rows in queries. */
  class CarTable(_tableTag: Tag) extends Table[Car](_tableTag, "car") {
    def * = (id, regNumber, make, model, mileage, service, comment, creationDate, creatorId, editDate, editorId, classId) <> (Car.tupled, Car.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, regNumber.?, make.?, model.?, mileage.?, service, comment, creationDate, creatorId, editDate, editorId, classId.?).shaped.<>({r=>import r._; _1.map(_=> Car.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9, _10, _11, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name car_creator_id_fkey) */
    lazy val creatorFk = foreignKey("car_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name car_editor_id_fkey) */
    lazy val editorFk = foreignKey("car_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing CarClassTable (database name car_class_id_fkey) */
    lazy val classFk = foreignKey("car_class_id_fkey", classId, CarClassTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (regNumber) (database name unique_reg_number) */
    val index1 = index("unique_reg_number", regNumber, unique=true)
  }
  /** Collection-like TableQuery object for table CarTable */
  lazy val CarTable = new TableQuery(tag => new CarTable(tag))
  
  /** Entity class storing rows of table CheckpointTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param pointDate Database column point_date DBType(date)
   *  @param days Database column days DBType(int4)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None) */
  case class Checkpoint(id: Int, pointDate: java.sql.Date, days: Int, creationDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, comment: Option[String] = None) extends Entity
  /** GetResult implicit for fetching Checkpoint objects using plain SQL queries */
  
  /** Table description of table checkpoint. Objects of this class serve as prototypes for rows in queries. */
  class CheckpointTable(_tableTag: Tag) extends Table[Checkpoint](_tableTag, "checkpoint") {
    def * = (id, pointDate, days, creationDate, creatorId, editDate, editorId, comment) <> (Checkpoint.tupled, Checkpoint.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, pointDate.?, days.?, creationDate, creatorId, editDate, editorId, comment).shaped.<>({r=>import r._; _1.map(_=> Checkpoint.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name checkpoint_creator_id_fkey) */
    lazy val creatorFk = foreignKey("checkpoint_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name checkpoint_editor_id_fkey) */
    lazy val editorFk = foreignKey("checkpoint_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Index over (pointDate) (database name point_date_index) */
    val index1 = index("point_date_index", pointDate)
  }
  /** Collection-like TableQuery object for table CheckpointTable */
  lazy val CheckpointTable = new TableQuery(tag => new CheckpointTable(tag))
  
  /** Entity class storing rows of table DriverTable
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
  case class Driver(id: Int, pass: String, license: String, lastName: Option[String] = None, firstName: Option[String] = None, middleName: Option[String] = None, phone: String, secPhone: String, comment: Option[String] = None, address: String, creationDate: Option[java.sql.Date] = None, editDate: Option[java.sql.Date] = None, creatorId: Option[Int] = None, editorId: Option[Int] = None) extends Entity
  /** GetResult implicit for fetching Driver objects using plain SQL queries */
  
  /** Table description of table driver. Objects of this class serve as prototypes for rows in queries. */
  class DriverTable(_tableTag: Tag) extends Table[Driver](_tableTag, "driver") {
    def * = (id, pass, license, lastName, firstName, middleName, phone, secPhone, comment, address, creationDate, editDate, creatorId, editorId) <> (Driver.tupled, Driver.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, pass.?, license.?, lastName, firstName, middleName, phone.?, secPhone.?, comment, address.?, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> Driver.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8.get, _9, _10.get, _11, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name driver_creator_id_fkey) */
    lazy val creatorFk = foreignKey("driver_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name driver_editor_id_fkey) */
    lazy val editorFk = foreignKey("driver_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (license) (database name idx_license_uq) */
    val index1 = index("idx_license_uq", license, unique=true)
    /** Uniqueness Index over (pass) (database name idx_pass_uq) */
    val index2 = index("idx_pass_uq", pass, unique=true)
  }
  /** Collection-like TableQuery object for table DriverTable */
  lazy val DriverTable = new TableQuery(tag => new DriverTable(tag))
  
  /** Entity class storing rows of table FineTable
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
  case class Fine(id: Int, fineDate: java.sql.Date, cost: scala.math.BigDecimal, description: Option[String] = None, rentId: Int, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None) extends Entity
  /** GetResult implicit for fetching Fine objects using plain SQL queries */
  
  /** Table description of table fine. Objects of this class serve as prototypes for rows in queries. */
  class FineTable(_tableTag: Tag) extends Table[Fine](_tableTag, "fine") {
    def * = (id, fineDate, cost, description, rentId, comment, creatorId, creationDate, editorId, editDate) <> (Fine.tupled, Fine.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, fineDate.?, cost.?, description, rentId.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Fine.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name fine_creator_id_fkey) */
    lazy val creatorFk = foreignKey("fine_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name fine_editor_id_fkey) */
    lazy val editorFk = foreignKey("fine_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing RentTable (database name fine_rent_id_fkey) */
    lazy val rentFk = foreignKey("fine_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FineTable */
  lazy val FineTable = new TableQuery(tag => new FineTable(tag))
  
  /** Entity class storing rows of table PaymentTable
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
  case class Payment(id: Int, payDate: java.sql.Date, amount: scala.math.BigDecimal, target: String, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None, rentId: Int) extends Entity
  /** GetResult implicit for fetching Payment objects using plain SQL queries */
  
  /** Table description of table payment. Objects of this class serve as prototypes for rows in queries. */
  class PaymentTable(_tableTag: Tag) extends Table[Payment](_tableTag, "payment") {
    def * = (id, payDate, amount, target, comment, creatorId, creationDate, editorId, editDate, rentId) <> (Payment.tupled, Payment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, payDate.?, amount.?, target.?, comment, creatorId, creationDate, editorId, editDate, rentId.?).shaped.<>({r=>import r._; _1.map(_=> Payment.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name payment_creator_id_fkey) */
    lazy val creatorFk = foreignKey("payment_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name payment_editor_id_fkey) */
    lazy val editorFk = foreignKey("payment_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing RentTable (database name payment_rent_id_fkey) */
    lazy val rentFk = foreignKey("payment_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table PaymentTable */
  lazy val PaymentTable = new TableQuery(tag => new PaymentTable(tag))
  
  /** Entity class storing rows of table RentStatusTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeDate Database column change_date DBType(date)
   *  @param status Database column status DBType(varchar), Length(255,true)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None) */
  case class RentStatus(id: Int, changeDate: java.sql.Date, status: String, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None) extends Entity
  /** GetResult implicit for fetching RentStatus objects using plain SQL queries */
  
  /** Table description of table rent_status. Objects of this class serve as prototypes for rows in queries. */
  class RentStatusTable(_tableTag: Tag) extends Table[RentStatus](_tableTag, "rent_status") {
    def * = (id, changeDate, status, comment, creatorId, creationDate, editorId, editDate) <> (RentStatus.tupled, RentStatus.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, changeDate.?, status.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> RentStatus.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name rent_status_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_status_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name rent_status_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_status_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Index over (changeDate) (database name change_date_index) */
    val index1 = index("change_date_index", changeDate)
    /** Index over (status) (database name status_index) */
    val index2 = index("status_index", status)
  }
  /** Collection-like TableQuery object for table RentStatusTable */
  lazy val RentStatusTable = new TableQuery(tag => new RentStatusTable(tag))
  
  /** Entity class storing rows of table RentTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param driverId Database column driver_id DBType(int4)
   *  @param carId Database column car_id DBType(int4)
   *  @param deposit Database column deposit DBType(numeric)
   *  @param comment Database column comment DBType(varchar), Length(2147483647,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(date), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(date), Default(None) */
  case class Rent(id: Int, driverId: Int, carId: Int, deposit: scala.math.BigDecimal, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None) extends Entity
  /** GetResult implicit for fetching Rent objects using plain SQL queries */
  
  /** Table description of table rent. Objects of this class serve as prototypes for rows in queries. */
  class RentTable(_tableTag: Tag) extends Table[Rent](_tableTag, "rent") {
    def * = (id, driverId, carId, deposit, comment, creatorId, creationDate, editorId, editDate) <> (Rent.tupled, Rent.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, driverId.?, carId.?, deposit.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Rent.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name rent_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name rent_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing CarTable (database name rent_car_id_fkey) */
    lazy val carFk = foreignKey("rent_car_id_fkey", carId, CarTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing DriverTable (database name rent_driver_id_fkey) */
    lazy val driverFk = foreignKey("rent_driver_id_fkey", driverId, DriverTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table RentTable */
  lazy val RentTable = new TableQuery(tag => new RentTable(tag))
  
  /** Entity class storing rows of table RepairTable
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
  case class Repair(id: Int, repairDate: java.sql.Date, cost: scala.math.BigDecimal, description: Option[String] = None, rentId: Int, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Date] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Date] = None) extends Entity
  /** GetResult implicit for fetching Repair objects using plain SQL queries */
  
  /** Table description of table repair. Objects of this class serve as prototypes for rows in queries. */
  class RepairTable(_tableTag: Tag) extends Table[Repair](_tableTag, "repair") {
    def * = (id, repairDate, cost, description, rentId, comment, creatorId, creationDate, editorId, editDate) <> (Repair.tupled, Repair.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, repairDate.?, cost.?, description, rentId.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Repair.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    
    /** Foreign key referencing AccountTable (database name repair_creator_id_fkey) */
    lazy val creatorFk = foreignKey("repair_creator_id_fkey", creatorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AccountTable (database name repair_editor_id_fkey) */
    lazy val editorFk = foreignKey("repair_editor_id_fkey", editorId, AccountTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing RentTable (database name repair_rent_id_fkey) */
    lazy val rentFk = foreignKey("repair_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table RepairTable */
  lazy val RepairTable = new TableQuery(tag => new RepairTable(tag))
}