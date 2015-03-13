package controllers

import models.repos.UsersRepo
import play.api.mvc.{AnyContent, Action}

import scala.slick.driver.PostgresDriver.simple._

object StorageController extends BaseController{
  private val statementSeparator = ";\n";

  def dropSql = Action {
    Ok(getDropSql)
  }

  def drop(): Action[AnyContent] = Action {
    withDbAction(doDrop)
    Ok("all ddl gone")
  }

  def createSql = Action {
    Ok(getCreateSql)
  }

  def create(): Action[AnyContent] = Action {
    withDbAction(doCreate)
    Ok("ddl created")
  }

  def init(): Action[AnyContent] = Action {
    withDb(doInit)
    Ok("init completed")
  }

  def recreateSql = Action {
    Ok(getDropSql + statementSeparator + getCreateSql)
  }

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
