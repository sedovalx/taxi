package controllers

import java.text.SimpleDateFormat

import models.{User, Users}
import play.api.db.DB
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc.{Action, Controller}
import play.api.Play.current

import scala.slick.driver.PostgresDriver.simple._

object UserController extends Controller{

  // сериализация объектов пользователей в json
  implicit val userWrites = new Writes[User] {
    val dateIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "lastName" -> user.lastName,
      "firstName" -> user.firstName,
      "middleName" -> user.middleName,
      "role" -> user.role.toString,
      "creationDate" -> dateIso8601Format.format(user.creationDate),
      "editDate" -> user.editDate,
      "creatorId" -> user.creatorId,
      "editorId" -> user.editorId
    )
  }

  def all = Action {
    val usersJson = withDb { session =>
      // получаем всех пользователей из БД
      val users = Users.all(session)
      // преобразуем их в json. тут неявно используется сериалайзер выше
      JsObject(Seq(
        "users" -> Json.toJson(users)
      ))
    }
    Ok(usersJson)
  }

  private def withDb[T](f: Session => T) = {
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      f(session)
    }
  }
}
