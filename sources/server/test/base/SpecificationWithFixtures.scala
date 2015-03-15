package base

import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}

/**
 * Created by ipopkov on 16/03/15.
 */
abstract class SpecificationWithFixtures extends Specification {
  abstract class WithFakeDB extends WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    override def around[T: AsResult](t: => T): Result = super.around {
      t
    }
  }
}
