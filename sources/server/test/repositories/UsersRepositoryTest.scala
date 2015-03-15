package repositories

import base.SpecificationWithFixtures
import models.repos.UsersRepo
import play.api.db.slick.{DB, Session}


class UsersRepositoryTest extends SpecificationWithFixtures {

  val users =  UsersRepo


  "UserRepository" should {

    "save and query" in new WithFakeDB {
      DB.withSession { implicit session: Session =>
       val user =  users createAdmin(session);
        val admin =   users.getById(user.id)
        admin must beSome;
      }
    }
  }
}