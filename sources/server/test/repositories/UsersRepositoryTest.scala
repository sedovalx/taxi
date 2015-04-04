package repositories

import base.SpecificationWithFixtures
import controllers.filter.UserFilter
import models.entities.Role.Role
import models.entities.{Role, User}
import utils.db.repos.UsersRepo
import play.api.db.slick.{DB, Session}
import utils.extensions.SqlDate

import scala.util.Random


class UsersRepositoryTest extends SpecificationWithFixtures {

  val users = UsersRepo

  private object User {
    def create(login: String, password: String, role: Role) =
      new User(id = Random.nextLong(), login = login, password = password, role = role, creationDate = Some(SqlDate.now))
  }

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

    "filter test" in new WithFakeDB {
      DB.withSession { implicit session: Session =>
        val user1 = createUser("user1", "pass", Role.Accountant)
        val user2 = createUser("user2", "pass", Role.Cashier)
        val user3 = createUser("user3", "pass", Role.Repairman)
        var uf = new UserFilter(None, Some(user1.login), None, None, None, None, None)
        var filteredUsers = UsersRepo.find(uf)
        filteredUsers.length must beEqualTo (1)

      }

    }
  }
}