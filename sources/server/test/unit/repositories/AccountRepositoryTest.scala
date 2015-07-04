package unit.repositories

import base.DatabaseSpecification
import models.entities.Role
import models.entities.Role._
import models.generated.Tables.SystemUser
import repository.SystemUserRepo
import utils.DateUtils

import scala.concurrent.Future
import scala.util.Random

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class AccountRepositoryTest extends DatabaseSpecification { sequential
  "save1 and query" in new DatabaseContext {
    // create new user
    val accountRepo = injector.instanceOf[SystemUserRepo]
    val user = new SystemUser(id = 0, login = "admin", passwordHash = "", role = Role.Administrator)
    val futureUserId = accountRepo.create(user)

    // check if user id is greater then zero
    val userId = await(futureUserId)(5 second)
    userId must be_>(0)
  }

  "second one1" in new DatabaseContext {
    1 mustEqual 2
  }
}
