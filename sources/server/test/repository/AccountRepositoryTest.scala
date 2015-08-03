package repository

import base.BaseDatabaseSpecification
import models.entities.Role
import models.generated.Tables.{SystemUser, SystemUserFilter}

import scala.util.Random

/**
 * Created by ipopkov on 02/08/15.
 */
class AccountRepositoryTest extends BaseDatabaseSpecification {

  override def application = builder.build()
  override def injector = application.injector

  "UserRepository" should {

    "save and query" in running(application) {

      val accountRepo = injector.instanceOf[SystemUserRepo]
      val user = new SystemUser(id = 0, login = "admin" + Random.alphanumeric.take(5).mkString, passwordHash = "", role = Role.Administrator)
      val futureUserId = accountRepo.create(user)

      // check if user id is greater then zero
      val userId = await(futureUserId)
      val returnedUser = await(accountRepo.findById(userId))
      returnedUser must beSome[SystemUser]
      userId must be_>(0)

    }


    "filter test" in running(application) {
      val accountRepo = injector.instanceOf[SystemUserRepo]
        Seq(
          SystemUser(id = 0, login = "u1", passwordHash = "pass", lastName = Some("Иванов"), firstName = Some("Сидор"), middleName = Some("Петрович"),role = Role.Accountant),
          SystemUser(id = 0, login = "u2", passwordHash = "pass", lastName = Some("Сидоров"), firstName = Some("Иван"), middleName = Some("Сидорович"),role = Role.Repairman),
          SystemUser(id = 0, login = "u3", passwordHash = "pass", lastName = Some("Иванов"), firstName = Some("Петр"), middleName = Some("Иванович"),role = Role.Accountant)
        ) foreach { a => accountRepo.create(a) }

        // expect:
        await(accountRepo.read()) must have size 4
        await(accountRepo.read(Some(SystemUserFilter()))) must have size 4
        await(accountRepo.read(Some(SystemUserFilter(login = Some("u1")))))  must have size 1
        await(accountRepo.read(Some(SystemUserFilter(lastName = Some("иВа"))))) must have size 2
        await(accountRepo.read(Some(SystemUserFilter(lastName = Some("ива"), firstName = Some("пет"))))) must have size 1
        await(accountRepo.read(Some(SystemUserFilter(lastName = Some("ивА"), firstName = Some("пет"), middleName = Some("иванови"))))) must have size 1
        await(accountRepo.read(Some(SystemUserFilter(lastName = Some("Хренова"), firstName = Some("Гадя"), middleName = Some("петрович"))))) must have size 0
        await(accountRepo.read(Some(SystemUserFilter(role = Some(Role.Accountant))))) must have size 2
        await(accountRepo.read(Some(SystemUserFilter(lastName = Some("сИдоров"), role = Some(Role.Repairman))))) must have size 1
      }

    }

}
