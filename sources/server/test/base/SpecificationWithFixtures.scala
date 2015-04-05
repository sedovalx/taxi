package base

import com.typesafe.config.ConfigFactory
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Specification
import org.specs2.specification.Fragments
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}

/**
 * Created by ipopkov on 16/03/15.
 */
abstract class SpecificationWithFixtures extends Specification{

  protected def beforeAll() {

  }

  val repositoryTestFakeApp = FakeApplication(
    additionalConfiguration = inMemoryDatabase(),
    withoutPlugins = Seq("play.api.db.BoneCPPlugin"),
    additionalPlugins = Seq("play.api.db.RestartableBoneCPPlugin")
  )


  abstract class WithFakeDB extends WithApplication(repositoryTestFakeApp) {
    override def around[T: AsResult](t: => T): Result = super.around {
      beforeAll()
      t
    }
  }
}
