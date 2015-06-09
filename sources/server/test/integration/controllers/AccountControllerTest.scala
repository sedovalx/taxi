package integration.controllers

import base.BaseControllerSpecification
import models.entities.Role
import models.entities.Role.Role
import models.generated.Tables.SystemUserFilter
import org.specs2.mock.Mockito
import play.api.db.slick._
import play.api.libs.json
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.test.Helpers.{defaultAwaitTimeout, _}
import repository.SystemUserRepo
import scaldi.Module
import service.SystemUserService

import scala.concurrent.Future

class AccountControllerTest extends BaseControllerSpecification with org.specs2.matcher.JsonMatchers with Mockito {

  "filter test happy path" in new WithFakeDB (new Module {
    val mockAccountService = mock [SystemUserService]
    mockAccountService.read(any[Option[SystemUserFilter]]) returns Future { Nil }
    bind [SystemUserService] to mockAccountService
  }) {
    // setup:
    val url = "/api/users?login=u1&lastName=1&firstName=2&middleName=3&role=Accountant"
    val expectedFilter = Some(SystemUserFilter(
      login = Some("u1"),
      lastName = Some("1"),
      firstName = Some("2"),
      middleName = Some("3"),
      role = Some(Role.Accountant)
    ))
    val mockAccountService = inject [SystemUserService]
    val readRequest = createEmptyAuthenticatedRequest(GET, url)

    // when:
    val Some(readResult) = route(readRequest)

    // then:
    status(readResult) must beEqualTo(OK)
    // see: http://etorreborre.github.io/specs2/guide/SPECS2-2.3.9/org.specs2.guide.Matchers.html#Mock+expectations
    there was one(mockAccountService).read(expectedFilter)
  }

  "validation error should has Ember format" in new WithFakeDB {
    DB.withSession { session =>
      val request = createCreateRequest(Some("12"), Some("1234567"), Some(Role.Administrator))
      val Some(result) = route(request)
      //println (contentAsString(result))
      status(result) must equalTo(UNPROCESSABLE_ENTITY)

      val jsonResult = contentAsJson(result)
      (jsonResult \ "login") must haveClass[JsArray]
      (jsonResult \ "password") must haveClass[JsArray]
    }
  }

  "validation happy path" in new WithFakeDB {
    val request = createCreateRequest(Some("1234"), Some("123456789"), Some(Role.Administrator))
    val Some(result) = route(request)
    //println(contentAsString(result))
    status(result) must equalTo(OK)

    (contentAsJson(result) \ "user") must haveClass[JsObject]
  }
  
  "should return null instead of password on read" in new WithFakeDB {
    // setup:
    val login = "1234"
    val createRequest = createCreateRequest(Some(login), Some("123456789"), Some(Role.Administrator))
    val Some(createResult) = route(createRequest)
    //println(contentAsString(createResult))
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
    val accounts = inject[SystemUserRepo]
    // setup:
    val login = "user1"
    val createRequest = createCreateRequest(Some(login), Some("password1"), Some(Role.Accountant))
    val Some(createResult) = route(createRequest)
    status(createResult) must equalTo(OK)
    val Some(accountBefore) =  DB.withSession { session => accounts.findByLogin(login)(session) }
    val updateJson = createJsonAccount(Some(accountBefore.login), None, Some(accountBefore.role))

    // test:
    val updateRequest = createAuthenticatedRequest(PUT, "/api/users/" + accountBefore.id, updateJson)
    val Some(updateResult) = route(updateRequest)
    status(updateResult) must equalTo(OK)

    // check:
    val Some(accountAfter) = DB.withSession { session => accounts.findByLogin(login)(session) }
    accountAfter.passwordHash must equalTo(accountBefore.passwordHash)
  }

  "should do update password if got something" in new WithFakeDB {
    val accounts = inject[SystemUserRepo]
    // setup:
    val createRequest = createCreateRequest(Some("user1"), Some("password1"), Some(Role.Accountant))
    val Some(createResult) = route(createRequest)
    status(createResult) must equalTo(OK)
    val jsonCreateResult = contentAsJson(createResult).as[JsObject]
    val id  = (jsonCreateResult \ "user" \ "id").as[String]
    val Some(accountBefore) =  DB.withSession { session => accounts.findById(id.toInt)(session) }

    // test:
    // see: https://www.playframework.com/documentation/2.3.0/ScalaJsonTransformers
    val transformation = JsPath.json.pickBranch(
      (JsPath \ 'user \ 'password).json.update(
        of[JsValue].map { _ => JsString("new value") }
      )
    )
    val JsSuccess(updateJson, _) = jsonCreateResult.transform(transformation)
    val updateRequest = createAuthenticatedRequest(PUT, "/api/users/" + id, updateJson)
    val Some(updateResult) = route(updateRequest)

    // check:
    println(contentAsString(updateResult))
    status(updateResult) must equalTo(OK)
    val Some(accountAfter) =  DB.withSession { session => accounts.findById(id.toInt)(session) }
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
}
