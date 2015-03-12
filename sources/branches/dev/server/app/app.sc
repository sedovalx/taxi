import scala.slick.driver.PostgresDriver.simple._
import models._
import play.api.libs.json.{JsObject, Json}

val database = Database.forURL(
  url = "jdbc:postgresql://localhost:5433/taxi",
  user = "postgres",
  password = "11111",
  prop = null,
  driver = "org.postgresql.Driver"
)
database withSession { implicit session =>
  val users = Users.all
  val usersArray = JsObject(Seq(
    "users" -> Json.toJson(users)
  ))
  Json.prettyPrint(usersArray)
}