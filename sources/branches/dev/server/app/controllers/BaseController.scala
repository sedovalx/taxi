package controllers

import play.api.db.DB
import play.api.mvc.Controller
import play.api.Play.current
import scala.slick.driver.PostgresDriver.simple._

class BaseController extends Controller{
  protected def withDb[T](f: Session => T) = {
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      f(session)
    }
  }

  protected def withDbAction[T](f: Session => Unit) = {
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      f(session)
    }
  }
}
