package models.base

import java.sql.Date
import utils.db.repos.UsersRepo
import play.api.db.slick.Config.driver.simple._

abstract class TableBase[T](tag: Tag, name: String) extends GenericTable[T](tag, name) {
  def creationDate  = column[Option[Date]]("creation_date")
  def editDate      = column[Option[Date]]("edit_date")
  def creatorId     = column[Option[Long]]("creator_id")
  def editorId      = column[Option[Long]]("editor_id")

  def creatorRef = foreignKey("fk_user_creator", creatorId, UsersRepo.tableQuery)(_.id)
  def editorRef = foreignKey("fk_user_editor", editorId, UsersRepo.tableQuery)(_.id)
}
