package models.utils

import play.api.db.slick.Config.driver.simple._

/**
  * Created by ipopkov on 15/03/15.
  */
abstract class GenericTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
   def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
}
