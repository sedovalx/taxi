package repositories

import base.SpecificationWithFixtures
import controllers.filter.UserFilter
import models.entities.Role.Role
import models.entities.{Role, User}
import utils.db.repos.UsersRepo
import play.api.db.slick.{DB, Session}
import utils.extensions.DateUtils

import scala.util.Random


class UsersRepositoryTest extends SpecificationWithFixtures {

  val users = UsersRepo

  private object User {
    def create(firstName: Option[String], lastName: Option[String], login: String, password: String, role: Role) =
      new User(id = Random.nextLong(), firstName = firstName, lastName = lastName,
        login = login, password = password, role = role, creationDate = Some(DateUtils.now))
  }

  private def createUser(login: String, pass: String, role: Role)(implicit session: Session) = {
    val user = User.create(None, None, login, pass, role)
    val id = users.create(user)
    user.copy(id = id)
  }

  private def createUser(firstName: String, lastName: String, login: String, pass: String, role: Role)(implicit session: Session) = {
    val user = User.create(Some(firstName), Some(lastName), login, pass, role)
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
        val user1 = createUser("User", "User", "user1", "pass", Role.Accountant)
        val user2 = createUser("User1", "User", "user2", "pass", Role.Cashier)
        val user3 = createUser("User", "User2", "user3", "pass", Role.Repairman)
        val user4 = createUser("User", "User", "user4", "pass", Role.Repairman)
        var uf = new UserFilter(None, Some("User"), Some("User"), None, None, None)
        var filteredUsers = UsersRepo.find(uf)
        filteredUsers.length must beEqualTo (2)

        uf = new UserFilter(None, None, None, None, Some(Role.Administrator), None)
        filteredUsers = UsersRepo.find(uf)
        filteredUsers.length must beEqualTo (1)

      }

    }
  }
}