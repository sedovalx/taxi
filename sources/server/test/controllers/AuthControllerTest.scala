package controllers

import base.BaseControllerSpecification
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.test.{Helpers, FakeRequest}
import play.api.test.Helpers._

class AuthControllerTest extends BaseControllerSpecification with org.specs2.matcher.JsonMatchers{

  assertStatusAndMediaTypeOfResponse(
    "should return 401 Unauthorized on wrong login",
    Map("identifier" -> "some_nonexistent_user", "password" -> "1111"),
    UNAUTHORIZED,
    MediaTypes.APPLICATION_JSON
  )

  assertStatusAndMediaTypeOfResponse(
    "should return 401 Unauthorized on correct login but wrong password",
    Map("identifier" -> "admin", "password" -> "1111"),
    UNAUTHORIZED,
    MediaTypes.APPLICATION_JSON
  )

  assertStatusAndMediaTypeOfResponse(
    "should return 500 Bad Request on wrong credentials format",
    Map("login" -> "admin", "password" -> "1111"),
    BAD_REQUEST,
    MediaTypes.APPLICATION_JSON
  )

  private def assertStatusAndMediaTypeOfResponse(title: String, credentials: Map[String, String], expectedStatus: Int, expectedMediaType: String) = {
    title in new WithFakeDB {
      // setup:
      val loginRequest = FakeRequest(POST, "/api/auth/login")
        .withHeaders(HeaderNames.CONTENT_TYPE -> MediaTypes.APPLICATION_JSON )
        .withJsonBody(Json.toJson(credentials))

      // test:
      val Some(loginResponse) = route(loginRequest)

      // check:
      status(loginResponse) must equalTo(expectedStatus)
      contentType(loginResponse) must beSome(expectedMediaType)
    }
  }
}
