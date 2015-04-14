package base

import models.entities.Role
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Specification
import play.api.Application
import play.api.db.slick._
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}


import utils.db.repo.AccountRepo
import models.generated.Tables.Account

/**
 * Created by ipopkov on 16/03/15.
 */
abstract class SpecificationWithFixtures extends Specification {

  protected def beforeAll() {

  }

  protected def repositoryTestFakeApp = {
    FakeApplication(
      additionalConfiguration = inMemoryDatabase(),
      withoutPlugins = Seq("play.api.db.BoneCPPlugin"),
      additionalPlugins = Seq("play.api.db.RestartableBoneCPPlugin")
    )
  }

  abstract class WithFakeDB extends WithApplication(repositoryTestFakeApp) {
    override def around[T: AsResult](t: => T): Result = super.around {
      beforeAll()
      t
    }
  }
}

class Test extends SpecificationWithFixtures {
  val accounts = AccountRepo


  "test 1" in new WithFakeDB {
    println("-----------------------")
    println("before")
    printLogins
    DB.withSession { implicit session =>
      accounts.create(Account(id = 0, login = "user1", passwordHash = "dfsdf", role = Role.Administrator))
    }
    println("after")
    printLogins
    println("-----------------------")
  }

  "test 2" in new WithFakeDB {
    println("-----------------------")
    println("before")
    printLogins
    DB.withSession { implicit session =>
      accounts.create(Account(id = 0, login = "user2", passwordHash = "dfsdf", role = Role.Administrator))
    }
    println("after")
    printLogins
    println("-----------------------")
  }

  private def printLogins(implicit app: Application) = {
    DB.withSession { session =>
      println("logins:")
      accounts.read(session).foreach { a => println(a.login) }
    }
  }
}
