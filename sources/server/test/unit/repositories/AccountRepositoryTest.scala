package unit.repositories

import base.SpecificationWithFixtures
import models.entities.Role.Role
import models.entities.Role
import play.api.db.slick.{DB, Session}
import repository.AccountRepo
import scaldi.Injectable
import utils.extensions.DateUtils
import models.generated.Tables.{Account, AccountFilter}

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

      val accountRepo = inject[AccountRepo]
      val userHelper = new User(accountRepo)

      DB.withSession { implicit session: Session =>
        val user = userHelper.createUser("admin", "", Role.Administrator)
        accountRepo.findById(user.id) must beSome
      }
    }

    "save, update, query" in new WithFakeDB {
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
      // setup:
      val accountRepo = inject[AccountRepo]

      DB.withSession { implicit session: Session =>
        Seq(
          Account(id = 0, login = "u1", passwordHash = "pass", lastName = Some("Иванов"), firstName = Some("Сидор"), middleName = Some("Петрович"),role = Role.Accountant),
          Account(id = 0, login = "u2", passwordHash = "pass", lastName = Some("Сидоров"), firstName = Some("Иван"), middleName = Some("Сидорович"),role = Role.Repairman),
          Account(id = 0, login = "u3", passwordHash = "pass", lastName = Some("Иванов"), firstName = Some("Петр"), middleName = Some("Иванович"),role = Role.Accountant)
        ) foreach { a => accountRepo.create(a) }

        // expect:
        accountRepo.read() must have size 3
        accountRepo.read(Some(AccountFilter())) must have size 3
        accountRepo.read(Some(AccountFilter(login = Some("u1")))) must have size 1
        accountRepo.read(Some(AccountFilter(lastName = Some("иВа")))) must have size 2
        accountRepo.read(Some(AccountFilter(lastName = Some("ива"), firstName = Some("пет")))) must have size 1
        accountRepo.read(Some(AccountFilter(lastName = Some("ивА"), firstName = Some("пет"), middleName = Some("иванови")))) must have size 1
        accountRepo.read(Some(AccountFilter(lastName = Some("Хренова"), firstName = Some("Гадя"), middleName = Some("петрович")))) must have size 0
        accountRepo.read(Some(AccountFilter(role = Some(Role.Accountant)))) must have size 2
        accountRepo.read(Some(AccountFilter(lastName = Some("сИдоров"), role = Some(Role.Repairman)))) must have size 1
      }

    }
  }
}