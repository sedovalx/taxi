package models.generated
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables

trait Tables {
  import play.api.db.slick.Config.driver.simple._
  import models.entities._
  import models.entities.Role.Role
  import repository.db.MappedColumnTypes._
  import com.mohiva.play.silhouette.api.Identity
  import utils.extensions.DateUtils
  import scala.slick.model.ForeignKeyAction
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = CarTable.ddl ++ CheckpointTable.ddl ++ DriverTable.ddl ++ ExpenseTable.ddl ++ FineTable.ddl ++ PaymentTable.ddl ++ RentStatusTable.ddl ++ RentTable.ddl ++ RepairTable.ddl ++ SystemUserTable.ddl
  

  /** Entity class storing rows of table CarTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param regNumber Database column reg_number DBType(varchar), Length(12,true)
   *  @param make Database column make DBType(varchar), Length(255,true)
   *  @param model Database column model DBType(varchar), Length(255,true)
   *  @param rate Database column rate DBType(numeric)
   *  @param mileage Database column mileage DBType(numeric)
   *  @param service Database column service DBType(numeric), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None) */
  case class Car(id: Int, regNumber: String, make: String, model: String, rate: scala.math.BigDecimal, mileage: scala.math.BigDecimal, service: Option[scala.math.BigDecimal] = None, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None) extends Entity[Car]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class CarFilter(id: Option[Int] = None, regNumber: Option[String] = None, make: Option[String] = None, model: Option[String] = None, rate: Option[scala.math.BigDecimal] = None, mileage: Option[scala.math.BigDecimal] = None, service: Option[scala.math.BigDecimal] = None, comment: Option[String] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None)
  
  /** Table description of table car. Objects of this class serve as prototypes for rows in queries. */
  class CarTable(_tableTag: Tag) extends Table[Car](_tableTag, "car") {
    def * = (id, regNumber, make, model, rate, mileage, service, comment, creationDate, creatorId, editDate, editorId) <> (Car.tupled, Car.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, regNumber.?, make.?, model.?, rate.?, mileage.?, service, comment, creationDate, creatorId, editDate, editorId).shaped.<>({r=>import r._; _1.map(_=> Car.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column reg_number DBType(varchar), Length(12,true) */
    val regNumber = column[String]("reg_number", O.Length(12,varying=true))
    /** Database column make DBType(varchar), Length(255,true) */
    val make = column[String]("make", O.Length(255,varying=true))
    /** Database column model DBType(varchar), Length(255,true) */
    val model = column[String]("model", O.Length(255,varying=true))
    /** Database column rate DBType(numeric) */
    val rate = column[scala.math.BigDecimal]("rate")
    /** Database column mileage DBType(numeric) */
    val mileage = column[scala.math.BigDecimal]("mileage")
    /** Database column service DBType(numeric), Default(None) */
    val service = column[Option[scala.math.BigDecimal]]("service", O.Default(None))
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    
    /** Foreign key referencing SystemUserTable (database name car_creator_id_fkey) */
    lazy val creatorFk = foreignKey("car_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name car_editor_id_fkey) */
    lazy val editorFk = foreignKey("car_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (regNumber) (database name unique_reg_number) */
    val index1 = index("unique_reg_number", regNumber, unique=true)
  }
  /** Collection-like TableQuery object for table CarTable */
  lazy val CarTable = new TableQuery(tag => new CarTable(tag))
  
  /** Entity class storing rows of table CheckpointTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param pointDate Database column point_date DBType(timestamp)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None) */
  case class Checkpoint(id: Int, pointDate: java.sql.Timestamp, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, comment: Option[String] = None) extends Entity[Checkpoint]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class CheckpointFilter(id: Option[Int] = None, pointDate: Option[java.sql.Timestamp] = None, creationDate: Option[java.sql.Timestamp] = None, creatorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, comment: Option[String] = None)
  
  /** Table description of table checkpoint. Objects of this class serve as prototypes for rows in queries. */
  class CheckpointTable(_tableTag: Tag) extends Table[Checkpoint](_tableTag, "checkpoint") {
    def * = (id, pointDate, creationDate, creatorId, editDate, editorId, comment) <> (Checkpoint.tupled, Checkpoint.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, pointDate.?, creationDate, creatorId, editDate, editorId, comment).shaped.<>({r=>import r._; _1.map(_=> Checkpoint.tupled((_1.get, _2.get, _3, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column point_date DBType(timestamp) */
    val pointDate = column[java.sql.Timestamp]("point_date")
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    
    /** Foreign key referencing SystemUserTable (database name checkpoint_creator_id_fkey) */
    lazy val creatorFk = foreignKey("checkpoint_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name checkpoint_editor_id_fkey) */
    lazy val editorFk = foreignKey("checkpoint_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Index over (pointDate) (database name point_date_index) */
    val index1 = index("point_date_index", pointDate)
  }
  /** Collection-like TableQuery object for table CheckpointTable */
  lazy val CheckpointTable = new TableQuery(tag => new CheckpointTable(tag))
  
  /** Entity class storing rows of table DriverTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param pass Database column pass DBType(varchar), Length(254,true)
   *  @param license Database column license DBType(varchar), Length(254,true)
   *  @param lastName Database column last_name DBType(varchar), Length(254,true)
   *  @param firstName Database column first_name DBType(varchar), Length(254,true)
   *  @param middleName Database column middle_name DBType(varchar), Length(254,true), Default(None)
   *  @param phone Database column phone DBType(varchar), Length(254,true)
   *  @param secPhone Database column sec_phone DBType(varchar), Length(254,true)
   *  @param comment Database column comment DBType(varchar), Length(254,true), Default(None)
   *  @param address Database column address DBType(varchar), Length(254,true)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None) */
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
    def ? = (id.?, pass.?, license.?, lastName.?, firstName.?, middleName, phone.?, secPhone.?, comment, address.?, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> Driver.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9, _10.get, _11, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column pass DBType(varchar), Length(254,true) */
    val pass = column[String]("pass", O.Length(254,varying=true))
    /** Database column license DBType(varchar), Length(254,true) */
    val license = column[String]("license", O.Length(254,varying=true))
    /** Database column last_name DBType(varchar), Length(254,true) */
    val lastName = column[String]("last_name", O.Length(254,varying=true))
    /** Database column first_name DBType(varchar), Length(254,true) */
    val firstName = column[String]("first_name", O.Length(254,varying=true))
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
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    
    /** Foreign key referencing SystemUserTable (database name driver_creator_id_fkey) */
    lazy val creatorFk = foreignKey("driver_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name driver_editor_id_fkey) */
    lazy val editorFk = foreignKey("driver_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (license) (database name idx_license_uq) */
    val index1 = index("idx_license_uq", license, unique=true)
    /** Uniqueness Index over (pass) (database name idx_pass_uq) */
    val index2 = index("idx_pass_uq", pass, unique=true)
  }
  /** Collection-like TableQuery object for table DriverTable */
  lazy val DriverTable = new TableQuery(tag => new DriverTable(tag))
  
  /** Entity class storing rows of table ExpenseTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeTime Database column change_time DBType(timestamp)
   *  @param amount Database column amount DBType(numeric)
   *  @param subject Database column subject DBType(varchar), Length(255,true)
   *  @param description Database column description DBType(varchar), Length(1000,true), Default(None)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None) */
  case class Expense(id: Int, changeTime: java.sql.Timestamp, amount: scala.math.BigDecimal, subject: String, description: Option[String] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None) extends Entity[Expense]
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class ExpenseFilter(id: Option[Int] = None, changeTime: Option[java.sql.Timestamp] = None, amount: Option[scala.math.BigDecimal] = None, subject: Option[String] = None, description: Option[String] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None)
  
  /** Table description of table expense. Objects of this class serve as prototypes for rows in queries. */
  class ExpenseTable(_tableTag: Tag) extends Table[Expense](_tableTag, "expense") {
    def * = (id, changeTime, amount, subject, description, comment, creatorId, creationDate, editorId, editDate) <> (Expense.tupled, Expense.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, changeTime.?, amount.?, subject.?, description, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Expense.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_time DBType(timestamp) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column amount DBType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column subject DBType(varchar), Length(255,true) */
    val subject = column[String]("subject", O.Length(255,varying=true))
    /** Database column description DBType(varchar), Length(1000,true), Default(None) */
    val description = column[Option[String]]("description", O.Length(1000,varying=true), O.Default(None))
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    
    /** Foreign key referencing SystemUserTable (database name expense_creator_id_fkey) */
    lazy val creatorFk = foreignKey("expense_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name expense_editor_id_fkey) */
    lazy val editorFk = foreignKey("expense_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ExpenseTable */
  lazy val ExpenseTable = new TableQuery(tag => new ExpenseTable(tag))
  
  /** Entity class storing rows of table FineTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeTime Database column change_time DBType(timestamp)
   *  @param amount Database column amount DBType(numeric)
   *  @param description Database column description DBType(varchar), Length(5000,true), Default(None)
   *  @param rentId Database column rent_id DBType(int4)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None) */
  case class Fine(id: Int, changeTime: java.sql.Timestamp, amount: scala.math.BigDecimal, description: Option[String] = None, rentId: Int, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None) extends Entity[Fine] with BalanceChange
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class FineFilter(id: Option[Int] = None, changeTime: Option[java.sql.Timestamp] = None, amount: Option[scala.math.BigDecimal] = None, description: Option[String] = None, rentId: Option[Int] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None)
  
  /** Table description of table fine. Objects of this class serve as prototypes for rows in queries. */
  class FineTable(_tableTag: Tag) extends Table[Fine](_tableTag, "fine") {
    def * = (id, changeTime, amount, description, rentId, comment, creatorId, creationDate, editorId, editDate) <> (Fine.tupled, Fine.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, changeTime.?, amount.?, description, rentId.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Fine.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_time DBType(timestamp) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column amount DBType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column description DBType(varchar), Length(5000,true), Default(None) */
    val description = column[Option[String]]("description", O.Length(5000,varying=true), O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    
    /** Foreign key referencing RentTable (database name fine_rent_id_fkey) */
    lazy val rentFk = foreignKey("fine_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name fine_creator_id_fkey) */
    lazy val creatorFk = foreignKey("fine_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name fine_editor_id_fkey) */
    lazy val editorFk = foreignKey("fine_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FineTable */
  lazy val FineTable = new TableQuery(tag => new FineTable(tag))
  
  /** Entity class storing rows of table PaymentTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeTime Database column change_time DBType(timestamp)
   *  @param presence Database column presence DBType(bool), Default(true)
   *  @param amount Database column amount DBType(numeric)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None)
   *  @param rentId Database column rent_id DBType(int4) */
  case class Payment(id: Int, changeTime: java.sql.Timestamp, presence: Boolean = true, amount: scala.math.BigDecimal, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, rentId: Int) extends Entity[Payment] with BalanceChange
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class PaymentFilter(id: Option[Int] = None, changeTime: Option[java.sql.Timestamp] = None, presence: Option[Boolean] = None, amount: Option[scala.math.BigDecimal] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None, rentId: Option[Int] = None)
  
  /** Table description of table payment. Objects of this class serve as prototypes for rows in queries. */
  class PaymentTable(_tableTag: Tag) extends Table[Payment](_tableTag, "payment") {
    def * = (id, changeTime, presence, amount, comment, creatorId, creationDate, editorId, editDate, rentId) <> (Payment.tupled, Payment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, changeTime.?, presence.?, amount.?, comment, creatorId, creationDate, editorId, editDate, rentId.?).shaped.<>({r=>import r._; _1.map(_=> Payment.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_time DBType(timestamp) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column presence DBType(bool), Default(true) */
    val presence = column[Boolean]("presence", O.Default(true))
    /** Database column amount DBType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    
    /** Foreign key referencing RentTable (database name payment_rent_id_fkey) */
    lazy val rentFk = foreignKey("payment_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name payment_creator_id_fkey) */
    lazy val creatorFk = foreignKey("payment_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name payment_editor_id_fkey) */
    lazy val editorFk = foreignKey("payment_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table PaymentTable */
  lazy val PaymentTable = new TableQuery(tag => new PaymentTable(tag))
  
  /** Entity class storing rows of table RentStatusTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeTime Database column change_time DBType(timestamp)
   *  @param status Database column status DBType(varchar), Length(255,true)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None)
   *  @param rentId Database column rent_id DBType(int4) */
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
    def ? = (id.?, changeTime.?, status.?, comment, creatorId, creationDate, editorId, editDate, rentId.?).shaped.<>({r=>import r._; _1.map(_=> RentStatus.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_time DBType(timestamp) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column status DBType(varchar), Length(255,true) */
    val status = column[models.entities.RentStatus.RentStatus]("status", O.Length(255,varying=true))
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    
    /** Foreign key referencing RentTable (database name rent_status_rent_id_fkey) */
    lazy val rentFk = foreignKey("rent_status_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing SystemUserTable (database name rent_status_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_status_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name rent_status_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_status_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Index over (changeTime) (database name change_date_index) */
    val index1 = index("change_date_index", changeTime)
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
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None) */
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
    def ? = (id.?, driverId.?, carId.?, deposit.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Rent.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column driver_id DBType(int4) */
    val driverId = column[Int]("driver_id")
    /** Database column car_id DBType(int4) */
    val carId = column[Int]("car_id")
    /** Database column deposit DBType(numeric) */
    val deposit = column[scala.math.BigDecimal]("deposit")
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    
    /** Foreign key referencing CarTable (database name rent_car_id_fkey) */
    lazy val carFk = foreignKey("rent_car_id_fkey", carId, CarTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing DriverTable (database name rent_driver_id_fkey) */
    lazy val driverFk = foreignKey("rent_driver_id_fkey", driverId, DriverTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name rent_creator_id_fkey) */
    lazy val creatorFk = foreignKey("rent_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name rent_editor_id_fkey) */
    lazy val editorFk = foreignKey("rent_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table RentTable */
  lazy val RentTable = new TableQuery(tag => new RentTable(tag))
  
  /** Entity class storing rows of table RepairTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param changeTime Database column change_time DBType(timestamp)
   *  @param amount Database column amount DBType(numeric)
   *  @param description Database column description DBType(varchar), Length(5000,true), Default(None)
   *  @param rentId Database column rent_id DBType(int4)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None) */
  case class Repair(id: Int, changeTime: java.sql.Timestamp, amount: scala.math.BigDecimal, description: Option[String] = None, rentId: Int, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None) extends Entity[Repair] with BalanceChange
  {
    def copyWithId(id: Int) = this.copy(id = id)
  
    def copyWithCreator(creatorId: Option[Int]) = this.copy(creatorId = creatorId, creationDate = Some(DateUtils.now))
  
    def copyWithEditor(editorId: Option[Int]) = this.copy(editorId = editorId, editDate = Some(DateUtils.now))
  }
               
  case class RepairFilter(id: Option[Int] = None, changeTime: Option[java.sql.Timestamp] = None, amount: Option[scala.math.BigDecimal] = None, description: Option[String] = None, rentId: Option[Int] = None, comment: Option[String] = None, creatorId: Option[Int] = None, creationDate: Option[java.sql.Timestamp] = None, editorId: Option[Int] = None, editDate: Option[java.sql.Timestamp] = None)
  
  /** Table description of table repair. Objects of this class serve as prototypes for rows in queries. */
  class RepairTable(_tableTag: Tag) extends Table[Repair](_tableTag, "repair") {
    def * = (id, changeTime, amount, description, rentId, comment, creatorId, creationDate, editorId, editDate) <> (Repair.tupled, Repair.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, changeTime.?, amount.?, description, rentId.?, comment, creatorId, creationDate, editorId, editDate).shaped.<>({r=>import r._; _1.map(_=> Repair.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(serial), AutoInc, PrimaryKey */
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column change_time DBType(timestamp) */
    val changeTime = column[java.sql.Timestamp]("change_time")
    /** Database column amount DBType(numeric) */
    val amount = column[scala.math.BigDecimal]("amount")
    /** Database column description DBType(varchar), Length(5000,true), Default(None) */
    val description = column[Option[String]]("description", O.Length(5000,varying=true), O.Default(None))
    /** Database column rent_id DBType(int4) */
    val rentId = column[Int]("rent_id")
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    
    /** Foreign key referencing RentTable (database name repair_rent_id_fkey) */
    lazy val rentFk = foreignKey("repair_rent_id_fkey", rentId, RentTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name repair_creator_id_fkey) */
    lazy val creatorFk = foreignKey("repair_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name repair_editor_id_fkey) */
    lazy val editorFk = foreignKey("repair_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table RepairTable */
  lazy val RepairTable = new TableQuery(tag => new RepairTable(tag))
  
  /** Entity class storing rows of table SystemUserTable
   *  @param id Database column id DBType(serial), AutoInc, PrimaryKey
   *  @param login Database column login DBType(varchar), Length(254,true)
   *  @param passwordHash Database column password_hash DBType(varchar), Length(1000,true)
   *  @param lastName Database column last_name DBType(varchar), Length(254,true), Default(None)
   *  @param firstName Database column first_name DBType(varchar), Length(254,true), Default(None)
   *  @param middleName Database column middle_name DBType(varchar), Length(254,true), Default(None)
   *  @param role Database column role DBType(varchar), Length(254,true)
   *  @param comment Database column comment DBType(varchar), Length(5000,true), Default(None)
   *  @param creationDate Database column creation_date DBType(timestamp), Default(None)
   *  @param editDate Database column edit_date DBType(timestamp), Default(None)
   *  @param creatorId Database column creator_id DBType(int4), Default(None)
   *  @param editorId Database column editor_id DBType(int4), Default(None) */
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
    def ? = (id.?, login.?, passwordHash.?, lastName, firstName, middleName, role.?, comment, creationDate, editDate, creatorId, editorId).shaped.<>({r=>import r._; _1.map(_=> SystemUser.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7.get, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    /** Database column comment DBType(varchar), Length(5000,true), Default(None) */
    val comment = column[Option[String]]("comment", O.Length(5000,varying=true), O.Default(None))
    /** Database column creation_date DBType(timestamp), Default(None) */
    val creationDate = column[Option[java.sql.Timestamp]]("creation_date", O.Default(None))
    /** Database column edit_date DBType(timestamp), Default(None) */
    val editDate = column[Option[java.sql.Timestamp]]("edit_date", O.Default(None))
    /** Database column creator_id DBType(int4), Default(None) */
    val creatorId = column[Option[Int]]("creator_id", O.Default(None))
    /** Database column editor_id DBType(int4), Default(None) */
    val editorId = column[Option[Int]]("editor_id", O.Default(None))
    
    /** Foreign key referencing SystemUserTable (database name system_user_creator_id_fkey) */
    lazy val creatorFk = foreignKey("system_user_creator_id_fkey", creatorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing SystemUserTable (database name system_user_editor_id_fkey) */
    lazy val editorFk = foreignKey("system_user_editor_id_fkey", editorId, SystemUserTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (login) (database name idx_login_uq) */
    val index1 = index("idx_login_uq", login, unique=true)
  }
  /** Collection-like TableQuery object for table SystemUserTable */
  lazy val SystemUserTable = new TableQuery(tag => new SystemUserTable(tag))
}