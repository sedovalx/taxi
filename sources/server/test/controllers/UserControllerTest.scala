package controllers

import base.{BaseControllerSpecification }
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.db.slick._
import play.api.test.{ FakeRequest }
import play.api.test.Helpers.defaultAwaitTimeout

/**
 * Created by ipopkov on 04/04/15.
 */
class UserControllerTest extends BaseControllerSpecification {

  "filter test" in new WithFakeDB  {
    DB.withSession { implicit session: Session =>
      val request = FakeRequest(GET, "/api/users?login=Vasya&role=Administrator&createDate=2015-07-30")
        .withHeaders(LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token )
      val Some(result) = route(request)
      status(result) must equalTo(OK)
    }
  }

  "validation error should has Ember format" in new WithFakeDB {
    DB.withSession { session =>
      val request = FakeRequest(POST, "/api/users")
        .withJsonBody(Json.obj(
          "user" -> Json.obj(
            "login" -> "12",
            "password" -> "1234567",
            "role" -> "Administrator"
          )))
        .withHeaders(
          LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token,
          CONTENT_TYPE -> "Application/json"
        )
      val Some(result) = route(request)
      status(result) must equalTo(UNPROCESSABLE_ENTITY)
      println (contentAsString(result))

      //todo: ох и блять...
      val jsonResult = contentAsJson(result)
      (jsonResult \ "login") must haveClass[JsArray]
      (jsonResult \ "password") must haveClass[JsArray]
    }
  }

  "validation happy path" in new WithFakeDB {
    val request = FakeRequest(POST, "/api/users")
      .withJsonBody(Json.obj(
      "user" -> Json.obj(
        "login" -> "1234",
        "password" -> "123456789",
        "role" -> "Administrator"
      )))
      .withHeaders(
        LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token,
        CONTENT_TYPE -> "Application/json"
      )
    val Some(result) = route(request)
    status(result) must equalTo(OK)

    (contentAsJson(result) \ "user") must haveClass[JsObject]
  }

}
