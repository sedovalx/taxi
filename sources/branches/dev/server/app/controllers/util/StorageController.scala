package controllers.util

import controllers.BaseController
import models.repos.UsersRepo
import play.api.mvc.{Action, AnyContent}

import scala.slick.driver.PostgresDriver.simple._

/**
 * Контроллер операций над БД
 */
object StorageController extends BaseController{
  private val statementSeparator = ";\n";

  /**
   * @return SQL-выражения по удалению схемы в БД
   */
  def dropSql = Action {
    Ok(getDropSql)
  }

  /**
   * Выполняет удаление схемы в БД вместе с данными
   * @return статус операции
   */
  def drop(): Action[AnyContent] = Action {
    withDbAction(doDrop)
    Ok("all ddl gone")
  }

  /**
   * @return SQL-выражения по созданию схемы в БД
   */
  def createSql = Action {
    Ok(getCreateSql)
  }

  /**
   * Выполняет создание схемы в БД
   * @return статус операции
   */
  def create(): Action[AnyContent] = Action {
    withDbAction(doCreate)
    Ok("ddl created")
  }

  /**
   * Выполняет наполнение БД первичными данными
   * @return статус операции
   */
  def init(): Action[AnyContent] = Action {
    withDb(doInit)
    Ok("init completed")
  }

  /**
   * @return SQL-выражения по пересозданию схемы БД
   */
  def recreateSql = Action {
    Ok(getDropSql + statementSeparator + getCreateSql)
  }

  /**
   * Выполняет пересоздание БД с наполнением первичными данными
   * @return статус операции
   */
  def recreate() = Action {
    withDbAction { session =>
      doDrop(session)
      doCreate(session)
      doInit(session)
    }
    Ok("recreate completed")
  }

  private def doDrop(session: Session) = UsersRepo.objects.ddl.drop(session)
  private def doCreate(session: Session) = UsersRepo.objects.ddl.create(session)
  private def doInit(session: Session) = UsersRepo.createAdmin(session)
  private def getDropSql = UsersRepo.objects.ddl.dropStatements.mkString(statementSeparator)
  private def getCreateSql = UsersRepo.objects.ddl.createStatements.mkString(statementSeparator)
}
