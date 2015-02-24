import java.sql.BatchUpdateException
import models.{Cars, CarClasses}

import scala.slick.driver.PostgresDriver.simple._
//object RegNumber {
//  private val digits = "0123456789"
//  private val letters = "АВЕИКМНОРСТУХ"
//
//  def next
//}

val data = Seq(
  ("A-class", Seq("A345ET197", "B344KO13")),
  ("B-class", Seq("H233PO34", "K956EE23", "C123MP34")),
  ("C-class", Seq("M453KO777"))
)

val database = Database.forURL(
  url = "jdbc:postgresql://localhost:5433/taxi",
  user = "postgres",
  password = "11111",
  prop = null,
  driver = "org.postgresql.Driver"
)
database withSession { implicit session =>
  try {
    Cars.objects.delete
    CarClasses.objects.delete
    CarClasses.objects.map(_.name).insertAll(data.map(_._1):_*)
    CarClasses.objects.foreach(c => {
      Cars.objects
        .map(r => (r.regNumber, r.classId))
        .insertAll(
          data.filter(i => i._1 == c.name).flatMap(_._2).map(n => (n, c.id)):_*
        )
    })
  } catch {
      case e: BatchUpdateException => {
        println(e)
        println(e.getNextException)
      }
  }
}