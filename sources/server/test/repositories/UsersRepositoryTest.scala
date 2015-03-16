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
        val admin =   users.findById(user.id)
        admin must beSome;
      }
    }

    "save, update, query" in new WithFakeDB {
      DB.withSession { implicit session: Session =>
        val user =  users createAdmin(session);
        val someNewLastName = Some("NewLast");
        val userWithUpdadedLastName = user.copy(lastName = someNewLastName);
        users update userWithUpdadedLastName;

        val someUpdatedUser =  users.findById(user.id);
        someUpdatedUser must beSome;
        val updatedUser = someUpdatedUser.get;
        updatedUser.lastName must beEqualTo(someNewLastName) ;
      }
    }
  }
}