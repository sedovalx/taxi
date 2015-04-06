package models.tables

import models.entities.Driver
import models.base.TableBase
import play.api.db.slick.Config.driver.simple._
import utils.db.repos.UsersRepo

/**
 * Маппинг доменной сущности на таблицу в БД
 */
class Drivers(tag: Tag) extends TableBase[Driver](tag, "driver") {
  def pass              = column[String]("pass")
  def license           = column[String]("driverCard")
  def lastName          = column[Option[String]]("last_name")
  def firstName         = column[Option[String]]("first_name")
  def middleName        = column[Option[String]]("middle_name")
  def phone             = column[String]("phone")
  def secPhone          = column[String]("secPhone")
  def comment           = column[Option[String]]("comment")
  def address           = column[String]("address")

  def * = (id, pass, license, lastName, firstName, middleName, phone, secPhone, comment, address, creationDate, editDate, creatorId, editorId) <> (Driver.tupled, Driver.unapply)

  def uniqueLicense = index("idx_license_uq", license, unique = true)
  def uniquePass = index("idx_pass_uq", pass, unique = true)
}
