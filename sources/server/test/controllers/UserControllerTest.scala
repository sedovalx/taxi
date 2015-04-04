package controllers

import base.SpecificationWithFixtures
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo

import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.entities.Role._
import models.entities.{Role, User}
import play.api.test._
import play.api.test.Helpers._
import play.api.db.slick._
import com.mohiva.play.silhouette.test._
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.exceptions._
import com.mohiva.play.silhouette.api.services.AuthInfoService
import com.mohiva.play.silhouette.api.util.{ Base64, Credentials, PasswordHasher, PasswordInfo }
import com.mohiva.play.silhouette.impl.exceptions.{ InvalidPasswordException, IdentityNotFoundException }
import com.mohiva.play.silhouette.impl.providers.BasicAuthProvider._
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import play.api.test.{ FakeRequest, PlaySpecification, WithApplication }
import utils.db.repos.UsersRepo
import utils.extensions.SqlDate

import scala.concurrent.Future
import scala.util.Random

/**
 * Created by ipopkov on 04/04/15.
 */
class UserControllerTest extends SpecificationWithFixtures {



  "filter test" in new WithFakeDB  {
    DB.withSession { implicit session: Session =>
//
//      val loginInfo = LoginInfo(CredentialsProvider.ID, "admin")
//
//      val identity = createUser("admin", "admin", Role.Administrator)
//      implicit val env = FakeEnvironment[User, FakeJWTAuthenticatorService](Seq(
//        loginInfo -> identity
//      ))
      //TODO: разобрать с авторизацией
      val request = FakeRequest(GET, "/api/users?login=Vasya")


      val Some(result) = route(request)
      status(result) must equalTo(OK)
    }
  }



}
