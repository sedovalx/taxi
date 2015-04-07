package models.entities

import java.sql.Date

case class CarClass(
 id: Long,
 name: String,
 rate: Double,
 comment: Option[String],
 creationDate: Option[Date],
 editDate: Option[Date],
 creatorId: Option[Long],
 editorId: Option[Long]
) extends Entity
