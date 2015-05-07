package base

import java.util.concurrent.TimeUnit

import models.entities.Role
import models.generated.Tables.Account
import play.api.http.HeaderNames
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, RouteInvokers, Writeables}
import service.AccountService

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration

/**
 * Created by ipopkov on 05/04/15.
 */
class BaseControllerSpecification extends SpecificationWithFixtures {

  object MediaTypes {
    val APPLICATION_JSON = "application/json"
  }

  object LoginUtil extends RouteInvokers with Writeables {

    var X_AUTH_TOKEN_HEADER = "X-Auth-Token"

    val loginRequest = FakeRequest(Helpers.POST, "/api/auth/login")
      .withHeaders(HeaderNames.CONTENT_TYPE -> MediaTypes.APPLICATION_JSON )
      .withJsonBody(Json.toJson(Map("identifier" -> "admin", "password" -> "admin")))

    var _token: String = _

    def token = _token

    def login() {
      val result = route(loginRequest).get
      //println(contentAsString(result))
      val body = Json.parse(contentAsString(result))
      _token = (body \ "token").as[String]
    }
  }

  protected override def beforeAll() {
    createAdminAccount()
    LoginUtil.login()
  }

  protected def createEmptyAuthenticatedRequest(method: String, route: String) = {
    FakeRequest(method, route)
      .withHeaders(
        LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token,
        CONTENT_TYPE -> MediaTypes.APPLICATION_JSON
      )
  }

  protected def createAuthenticatedRequest(method: String, route: String, json: JsValue) = {
    createEmptyAuthenticatedRequest(method, route) .withJsonBody(json)
  }

  private def createAdminAccount() = {
    implicit val injector = global.injector
    val userService = inject [AccountService]
    val admin = Account(id = 0, login = "admin", passwordHash = "admin", role = Role.Administrator)
    Await.ready(userService.create(admin, None), Duration.create(1, TimeUnit.SECONDS))
  }

  protected def statusMustBeOK(response: Future[Result]) = {
    val actualStatus = status(response)
    if (actualStatus != OK){
      println(contentAsString(response))
      actualStatus must beEqualTo(OK)
    }
  }
}
