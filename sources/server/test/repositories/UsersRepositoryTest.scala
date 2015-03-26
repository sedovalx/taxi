package repositories

import base.SpecificationWithFixtures
import models.entities.Role.Role
import models.entities.{Role, User}
import models.repos.UsersRepo
import play.api.db.slick.{DB, Session}


class UsersRepositoryTest extends SpecificationWithFixtures {

  val users = UsersRepo

  private def createUser(login: String, pass: String, role: Role)(implicit session: Session) = {
    val user = User.create(login, pass, role)
    val id = users.create(user)
    user.copy(id = id)
  }

  "UserRepository" should {

    "save and query" in new WithFakeDB {
      DB.withSession { implicit session: Session =>
        val user = createUser("admin", "", Role.Administrator)
        users.findById(user.id) must beSome
      }
    }

    "save, update, query" in new WithFakeDB {
      DB.withSession { implicit session: Session =>
        val user = createUser("user", "pass", Role.Accountant)
        val someNewLastName = Some("last name")
        val userWithUpdatedLastName = user.copy(lastName = someNewLastName)
        users.update(userWithUpdatedLastName)

        val someUpdatedUser = users.findById(user.id)
        someUpdatedUser must beSome
        val updatedUser = someUpdatedUser.get
        updatedUser.lastName must beEqualTo(someNewLastName) ;
      }
    }
  }
}