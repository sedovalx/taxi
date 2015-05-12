package base

import com.typesafe.config.ConfigFactory
import configuration.di.{PlayModule, RepoModule, ServicesModule, SilhouetteModule}
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Specification
import play.api.Configuration
import play.api.mvc.{Filter, RequestHeader, WithFilters}
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}
import scaldi.play.ScaldiSupport
import scaldi.{Injectable, Injector}

import scala.concurrent.Future


/**
 * Created by ipopkov on 16/03/15.
 */
abstract class SpecificationWithFixtures extends Specification  with Injectable {


  protected def beforeAll() {

  }

  object RoutesLoggingFilter extends Filter {
    override def apply(next: (RequestHeader) => Future[play.api.mvc.Result])(rh: RequestHeader): Future[play.api.mvc.Result] = {
      println(s"${rh.method} ${rh.uri}")
      next(rh)
    }
  }

  val global = new WithFilters(RoutesLoggingFilter) with ScaldiSupport {
    override def applicationModule: Injector = new RepoModule ++ new ServicesModule ++ new SilhouetteModule ++ new PlayModule

    override def configuration = Configuration(ConfigFactory.load())
  }


  protected def repositoryTestFakeApp = {
    FakeApplication(
      additionalConfiguration = inMemoryDatabase(),
      withoutPlugins = Seq("play.api.db.BoneCPPlugin"),
      additionalPlugins = Seq("play.api.db.RestartableBoneCPPlugin"),
      withGlobal = Some(global)
    )
  }

  abstract class WithFakeDB extends WithApplication(repositoryTestFakeApp) {

    override def around[T: AsResult](t: => T): Result = super.around {
      beforeAll()
      t
    }
  }

}
