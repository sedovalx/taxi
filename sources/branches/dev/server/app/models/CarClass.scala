package models

import play.api.db.DB
import play.api.Play.current
import scala.slick.lifted.TableQuery
import slick.driver.PostgresDriver.simple._

case class CarClass(id: Int, name: String)

class CarClasses(tag: Tag) extends Table[CarClass](tag, "car_class") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def * = (id, name) <> (CarClass.tupled, CarClass.unapply)

  def idxNameUnique = index("idx_name_uq", name, unique = true)
}

object CarClasses {
  val objects = TableQuery[CarClasses]

  def getAll: List[CarClass] = {
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      objects.list(session)
    }
  }
}

