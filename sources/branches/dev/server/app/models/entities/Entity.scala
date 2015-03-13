package models.entities

import java.sql.Date

trait Entity {
  val creationDate: Date
  val editDate: Option[Date]
  val creatorId: Option[Long]
  val editorId: Option[Long]
}
