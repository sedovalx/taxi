package controllers

import models.repos.UsersRepo
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import utils.serialization.UserSerializer

import scala.slick.driver.PostgresDriver.simple._

object UserController extends BaseController{

  // сериализация объектов пользователей в json
  implicit val userWrites = UserSerializer.writes

  def read = Action {
    val usersJson = withDb { session =>
      // получаем всех пользователей из БД
      val users = UsersRepo.read(session)
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
