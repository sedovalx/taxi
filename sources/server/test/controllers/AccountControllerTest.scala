package controllers

import base.BaseControllerSpecification
import models.entities.Role
import models.entities.Role.Role
import play.api.Application
import play.api.db.slick._
import play.api.libs.json
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers.{defaultAwaitTimeout, _}
import repository.AccountRepo
import scaldi.Injector

/**
 * Created by ipopkov on 04/04/15.
 */
class AccountControllerTest extends BaseControllerSpecification with org.specs2.matcher.JsonMatchers {

  "filter test" in new WithFakeDB {
    DB.withSession { implicit session: Session =>
      val request = FakeRequest(GET, "/api/users?login=Vasya&role=Administrator&createDate=2015-07-30")
        .withHeaders(LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token )
      val Some(result) = route(request)
      status(result) must equalTo(OK)
    }
  }

  "validation error should has Ember format" in new WithFakeDB {
    DB.withSession { session =>
      val request = createCreateRequest(Some("12"), Some("1234567"), Some(Role.Administrator))
      val Some(result) = route(request)
      println (contentAsString(result))
      status(result) must equalTo(UNPROCESSABLE_ENTITY)

      val jsonResult = contentAsJson(result)
      (jsonResult \ "login") must haveClass[JsArray]
      (jsonResult \ "password") must haveClass[JsArray]
    }
  }

  "validation happy path" in new WithFakeDB {
    val request = createCreateRequest(Some("1234"), Some("123456789"), Some(Role.Administrator))
    val Some(result) = route(request)
    println(contentAsString(result))
    status(result) must equalTo(OK)

    (contentAsJson(result) \ "user") must haveClass[JsObject]
  }
  
  "should return null instead of password on read" in new WithFakeDB {
    // setup:
    val login = "1234"
    val createRequest = createCreateRequest(Some(login), Some("123456789"), Some(Role.Administrator))
    val Some(createResult) = route(createRequest)
    println(contentAsString(createResult))
    status(createResult) must equalTo(OK)
    val readRequest = createEmptyAuthenticatedRequest("GET", "/api/users")
    
    // test:
    val Some(readResult) = route(readRequest)

    // check:
    status(readResult) must equalTo(OK)
    val resultJson = contentAsString(readResult)
    resultJson must /("users") /# 0 /("password" -> null)
  }

  "should not update password if got null back" in new WithFakeDB {
    implicit val injector = global.injector
    val accounts = inject[AccountRepo]
    // setup:
    val login = "user1"
    val createRequest = createCreateRequest(Some(login), Some("password1"), Some(Role.Accountant))
    val Some(createResult) = route(createRequest)
    status(createResult) must equalTo(OK)
    val Some(accountBefore) =  DB.withSession { session => accounts.findByLogin(login)(session) }
    val updateJson = createJsonAccount(Some(accountBefore.login), None, Some(accountBefore.role))
    
    // test:
    val updateRequest = createAuthenticatedRequest("POST", "/api/users/" + accountBefore.id, updateJson + ("id", JsNumber(accountBefore.id)))
    val Some(updateResult) = route(updateRequest)
    status(updateResult) must equalTo(OK)

    // check:
    val Some(accountAfter) =  DB.withSession { session => accounts.findByLogin(login)(session) }
    accountAfter.passwordHash must equalTo(accountBefore.passwordHash)
  }

  "should do update password if got something" in new WithFakeDB {
    implicit val injector = global.injector
    val accounts = inject[AccountRepo]
    // setup:
    val accountJson = createJsonAccount(Some("user1"), Some("password1"), Some(Role.Accountant))
    val createRequest = createAuthenticatedRequest(POST, "/api/users/", accountJson)
    val Some(createResult) = route(createRequest)
    status(createResult) must equalTo(OK)
    val jsonCreateResult = contentAsJson(createResult).as[JsObject]
    val id  = (jsonCreateResult \ "user" \ "id").as[Int]
    val Some(accountBefore) =  DB.withSession { session => accounts.findById(id)(session) }

    // test:
    val updateJson = jsonCreateResult + ("password", JsString("new value")) + ("id", JsString(id.toString))
    val updateRequest = createAuthenticatedRequest("POST", "/api/users/" + id, updateJson)
    val Some(updateResult) = route(updateRequest)

    // check:
    status(updateResult) must equalTo(OK)
    val Some(accountAfter) =  DB.withSession { session => accounts.findById(id)(session) }
    accountAfter.passwordHash must not equalTo accountBefore.passwordHash
  }

  private def createJsonAccount(login: Option[String], password: Option[String], role: Option[Role]) =
    Json.obj(
      "user" -> Json.obj(
        "login" -> (if (login.isEmpty) json.JsNull else JsString(login.get)),
        "password" ->(if (password.isEmpty) json.JsNull else JsString(password.get)),
        "role" -> role.map { r => r.toString }.fold(JsNull.as[JsValue])(r => JsString(r))
      )
    )

  private def createCreateRequest(login: Option[String], password: Option[String], role: Option[Role]) =
    createAuthenticatedRequest(POST, "/api/users", createJsonAccount(login, password, role))

  private def printLogins(implicit app: Application, accounts: AccountRepo) = {
    DB.withSession { session =>
      accounts.read(session).foreach { a => println(a.login) }
    }
  }
}
