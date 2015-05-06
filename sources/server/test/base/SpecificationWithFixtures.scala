package base

import com.typesafe.config.ConfigFactory
import configuration.di.{RepoModule, PlayModule, ServicesModule, SilhouetteModule}
import models.entities.Role
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Specification
import play.api.{Logger, Configuration, GlobalSettings, Application}
import play.api.db.slick._
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}

import models.generated.Tables.Account
import repository.AccountRepo
import scaldi.{Injector, Injectable, Module}
import scaldi.play.ScaldiSupport
import service.AccountService


/**
 * Created by ipopkov on 16/03/15.
 */
abstract class SpecificationWithFixtures extends Specification  with Injectable {


  protected def beforeAll() {

  }

  val global = new ScaldiSupport {
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
