package models.entities

import java.sql.Timestamp

/**
 * Этими свойствами должны обладать все сущности в БД
 */
trait Entity {
  /**
   * Идентификатор
   */
  val id: Int
  /**
   * Дата создания
   */
  val creationDate: Option[Timestamp]
  /**
   * Дата последнего редактирования
   */
  val editDate: Option[Timestamp]
  /**
   * Создатель записи
   */
  val creatorId: Option[Int]
  /**
   * Последний отредактировавший запись
   */
  val editorId: Option[Int]
  /**
   * Произвольный комментарий
   */
  val comment: Option[String]
}
