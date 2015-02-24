import play.api.Application
import play.api.GlobalSettings
import play.api.Play.current
import play.api.db.DB

import scala.slick.driver.PostgresDriver.simple._

import models._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    lazy val database = Database.forDataSource(DB.getDataSource())

    database withSession { implicit session =>
      import scala.slick.jdbc.meta.MTable
      if (MTable.getTables(CarClasses.objects.baseTableRow.tableName).list.isEmpty) {
        CarClasses.objects.ddl.create
      }

      if (MTable.getTables(Cars.objects.baseTableRow.tableName).list.isEmpty) {
        Cars.objects.ddl.create
      }
    }
  }
}