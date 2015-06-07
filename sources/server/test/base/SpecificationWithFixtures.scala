package base

import com.typesafe.config.ConfigFactory
import configuration.TestHelperModule
import configuration.di._
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Specification
import play.api.Configuration
import play.api.mvc.{Filter, RequestHeader, WithFilters}
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}
import scaldi.play.ScaldiSupport
import scaldi.{Module, Injectable, Injector}

import scala.concurrent.Future

abstract class SpecificationWithFixtures extends Specification  with Injectable {


  protected def beforeAll(implicit inj: Injector) {

  }

  object RoutesLoggingFilter extends Filter {
    override def apply(next: (RequestHeader) => Future[play.api.mvc.Result])(rh: RequestHeader): Future[play.api.mvc.Result] = {
      println(s"${rh.method} ${rh.uri}")
      next(rh)
    }
  }

  private def global(overrides: Module) = new WithFilters(RoutesLoggingFilter) with ScaldiSupport {
    override def applicationModule: Injector =
      new TestHelperModule ::
        overrides ::
        new PlayModule ::
        new SilhouetteModule ::
        new ServicesModule ::
        new QueryModule ::
        new RepoModule ::
        new SerializationModule

    override def configuration = Configuration(ConfigFactory.load())
  }

  protected def repositoryTestFakeApp(overrides: Module) = {
    FakeApplication(
      additionalConfiguration = inMemoryDatabase("default", Map(/*"TRACE_LEVEL_SYSTEM_OUT" -> "4"*/)),
      withoutPlugins = Seq("play.api.db.BoneCPPlugin"),
      additionalPlugins = Seq("play.api.db.RestartableBoneCPPlugin"),
      withGlobal = Some(global(overrides))
    )
  }

  abstract class WithFakeDB(overrides: => Module) extends WithApplication(repositoryTestFakeApp(overrides)) {

    def this() = this(new Module {})

    protected implicit lazy val injector: Injector = implicitApp.global.asInstanceOf[ScaldiSupport].applicationModule

    override def around[T: AsResult](t: => T): Result = super.around {
      beforeAll(injector)
      t
    }
  }

}
