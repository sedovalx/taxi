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


  abstract class WithFakeDB extends WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    override def around[T: AsResult](t: => T): Result = super.around {
      beforeAll()
      t
    }
  }
}
