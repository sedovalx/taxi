package base

import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Helpers, FakeRequest, Writeables, RouteInvokers}

/**
 * Created by ipopkov on 05/04/15.
 */
class BaseControllerSpecification extends SpecificationWithFixtures  {

  object LoginUtil extends RouteInvokers with Writeables {

    var X_AUTH_TOKEN_HEADER = "X-Auth-Token"

    val loginRequest = FakeRequest(Helpers.POST, "/api/auth/login")
      .withHeaders(HeaderNames.CONTENT_TYPE -> "application/json" )
      .withJsonBody(Json.toJson(Map("identifier" -> "admin", "password" -> "admin")))

    var _token: String = _

    def token = _token

    def login() {
      val result = route(loginRequest).get
      val body = Json.parse(contentAsString(result))
      _token = (body \ "token").as[String]
    }
  }

  protected override def beforeAll() {
    LoginUtil.login()
  }

}
