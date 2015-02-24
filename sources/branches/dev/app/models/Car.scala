package models

import play.api.Play.current
import play.api.db.DB
import slick.driver.PostgresDriver.simple._

case class Car(id: Int, regNumber: String, classId: Int)

class Cars(tag: Tag) extends Table[Car](tag, "car") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def regNumber = column[String]("reg_number")
  def classId = column[Int]("class_id")
  def * = (id, regNumber, classId) <> (Car.tupled, Car.unapply)
  def classRef = foreignKey("class_fk", classId, CarClasses.objects)(_.id)

  def idxRegNumberUnique = index("idx_reg_number_uq", regNumber, unique = true)
}

object Cars {
  val objects = TableQuery[Cars]

  def getAll: List[Car] = {
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      objects.list(session)
    }
  }
}
