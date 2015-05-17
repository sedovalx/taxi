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
import scaldi.{Module, Injectable, Injector}

import scala.concurrent.Future


/**
 * Created by ipopkov on 16/03/15.
 */
abstract class SpecificationWithFixtures extends Specification  with Injectable {


  protected def beforeAll(inj: Injector) {

  }

  object RoutesLoggingFilter extends Filter {
    override def apply(next: (RequestHeader) => Future[play.api.mvc.Result])(rh: RequestHeader): Future[play.api.mvc.Result] = {
      println(s"${rh.method} ${rh.uri}")
      next(rh)
    }
  }

  private def global(overrides: Option[Module] = None) = new WithFilters(RoutesLoggingFilter) with ScaldiSupport {
    override def applicationModule: Injector = new RepoModule ++ new ServicesModule ++ new SilhouetteModule ++ new PlayModule ++ overrides.getOrElse(new Module {})

    override def configuration = Configuration(ConfigFactory.load())
  }


  protected def repositoryTestFakeApp(overrides: Option[Module] = None) = {
    FakeApplication(
      additionalConfiguration = inMemoryDatabase("default", Map("TRACE_LEVEL_SYSTEM_OUT" -> "4")),
      withoutPlugins = Seq("play.api.db.BoneCPPlugin"),
      additionalPlugins = Seq("play.api.db.RestartableBoneCPPlugin"),
      withGlobal = Some(global(overrides))
    )
  }

  abstract class WithFakeDB(overrides: Option[Module] = None) extends WithApplication(repositoryTestFakeApp(overrides)) {

    protected implicit lazy val injector = implicitApp.global.asInstanceOf[ScaldiSupport].injector

    override def around[T: AsResult](t: => T): Result = super.around {
      beforeAll(injector)
      t
    }
  }

}
