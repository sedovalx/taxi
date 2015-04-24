package repositories

import base.SpecificationWithFixtures
import controllers.filter.AccountFilter
import models.entities.Role.Role
import models.entities.Role
import play.api.db.slick.{DB, Session}
import repository.AccountRepo
import scaldi.{Injectable}
import utils.extensions.DateUtils
import models.generated.Tables.Account

import scala.util.Random


class AccountRepositoryTest extends SpecificationWithFixtures with Injectable {

   class User(accountRepo: AccountRepo) {
     def create(firstName: Option[String], lastName: Option[String], login: String, password: String, role: Role) =
       new Account(id = Random.nextInt(), firstName = firstName, lastName = lastName,
         login = login, passwordHash = password, role = role, creationDate = Some(DateUtils.now))
     def createUser(login: String, pass: String, role: Role)(implicit session: Session) = {
       val user = create(None, None, login, pass, role)
       val id = accountRepo.create(user)
       user.copy(id = id)
     }

     def createUser(firstName: String, lastName: String, login: String, pass: String, role: Role)(implicit session: Session) = {
       val user = create(Some(firstName), Some(lastName), login, pass, role)
       val id = accountRepo.create(user)
       user.copy(id = id)
     }

  }

  "UserRepository" should {
    "save and query" in new WithFakeDB {

      implicit val injector = global.injector
      val accountRepo = inject[AccountRepo]
      val userHelper = new User(accountRepo)

      DB.withSession { implicit session: Session =>
        val user = userHelper.createUser("admin", "", Role.Administrator)
        accountRepo.findById(user.id) must beSome
      }
    }

    "save, update, query" in new WithFakeDB {
      implicit val injector = global.injector
      val accountRepo = inject[AccountRepo]
      val userHelper = new User(accountRepo)

      DB.withSession { implicit session: Session =>
        val user = userHelper.createUser("user", "pass", Role.Accountant)
        val someNewLastName = Some("last name")
        val userWithUpdatedLastName = user.copy(lastName = someNewLastName)
        accountRepo.update(userWithUpdatedLastName)

        val someUpdatedUser = accountRepo.findById(user.id)
        someUpdatedUser must beSome
        val updatedUser = someUpdatedUser.get
        updatedUser.lastName must beEqualTo(someNewLastName) ;
      }
    }

    "filter test" in new WithFakeDB {

      implicit val injector = global.injector
      val accountRepo = inject[AccountRepo]
      val userHelper = new User(accountRepo)

      DB.withSession { implicit session: Session =>
        userHelper.createUser("User", "User", "user1", "pass", Role.Accountant)
        userHelper.createUser("User1", "User", "user2", "pass", Role.Cashier)
        userHelper.createUser("User", "User2", "user3", "pass", Role.Repairman)
        userHelper.createUser("User", "User", "user4", "pass", Role.Repairman)
        var uf = new AccountFilter(None, Some("User"), Some("User"), None, None, None)
        var filteredUsers = accountRepo.find(uf)
        filteredUsers.length must beEqualTo (2)

        uf = new AccountFilter(None, None, None, None, Some(Role.Administrator), None)
        filteredUsers = accountRepo.find(uf)
      }

    }
  }
}