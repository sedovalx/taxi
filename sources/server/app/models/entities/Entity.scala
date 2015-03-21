package models.entities

import java.sql.Date

/**
 * Этими свойствами должны обладать все сущности в БД
 */
trait Entity {
  /**
   * Идентификатор
   */
  val id: Long
  /**
   * Дата создания
   */
  val creationDate: Date
  /**
   * Дата последнего редактирования
   */
  val editDate: Option[Date]
  /**
   * Создатель записи
   */
  val creatorId: Option[Long]
  /**
   * Последний отредактировавший запись
   */
  val editorId: Option[Long]
}
