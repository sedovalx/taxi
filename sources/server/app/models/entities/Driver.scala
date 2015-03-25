package models.entities

import java.sql.Date

/**
 * Доменный класс пользователя системы
 * @param id идентификатор
 * @param pass серия-номер паспорта
 * @param license серия-номер водительского удостоверения
 * @param lastName фамилия
 * @param firstName имя
 * @param middleName отчество
 * @param phone телефон
 * @param secPhone дополнительный телефон

 */
case class Driver(
 id: Long,
 pass: String,
 license: String,
 lastName: Option[String],
 firstName: Option[String],
 middleName: Option[String],
 phone: Long,
 secPhone: Long,
 comment: String,
 creationDate: Date,
 editDate: Option[Date],
 creatorId: Option[Long],
 editorId: Option[Long]
) extends Entity