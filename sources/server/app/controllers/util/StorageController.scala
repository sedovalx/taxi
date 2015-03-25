package controllers.util

import controllers.BaseController
import models.repos.UsersRepo
import play.api.mvc.Action

import play.api.db.slick.Config.driver.simple._
import scaldi.{Injectable, Injector}

/**
 * Контроллер операций над БД
 */
class StorageController(implicit inj: Injector) extends BaseController with Injectable  {
  private val statementSeparator = ";\n"

  /**
   * @return SQL-выражения по удалению схемы в БД
   */
  def dropSql = Action { implicit request =>
    Ok(getDropSql)
  }

  /**
   * Выполняет удаление схемы в БД вместе с данными
   * @return статус операции
   */
  def drop() = Action { implicit request =>
    withDbAction(doDrop)
    Ok("all ddl gone")
  }

  /**
   * @return SQL-выражения по созданию схемы в БД
   */
  def createSql = Action { implicit request =>
    Ok(getCreateSql)
  }

  /**
   * Выполняет создание схемы в БД
   * @return статус операции
   */
  def create() = Action { implicit request =>
    withDbAction(doCreate)
    Ok("ddl created")
  }

  /**
   * Выполняет наполнение БД первичными данными
   * @return статус операции
   */
  def init() = Action { implicit request =>
    withDb(doInit)
    Ok("init completed")
  }

  /**
   * @return SQL-выражения по пересозданию схемы БД
   */
  def recreateSql = Action { implicit request =>
    Ok(getDropSql + statementSeparator + getCreateSql)
  }

  /**
   * Выполняет пересоздание БД с наполнением первичными данными
   * @return статус операции
   */
  def recreate() = Action { implicit request =>
    withDbAction { session =>
      doDrop(session)
      doCreate(session)
      doInit(session)
    }
    Ok("recreate completed")
  }

  private def doDrop(session: Session) = UsersRepo.tableQuery.ddl.drop(session)
  private def doCreate(session: Session) = UsersRepo.tableQuery.ddl.create(session)
  private def doInit(session: Session) = UsersRepo.createAdmin(session)
  private def getDropSql = UsersRepo.tableQuery.ddl.dropStatements.mkString(statementSeparator)
  private def getCreateSql = UsersRepo.tableQuery.ddl.createStatements.mkString(statementSeparator)
}
