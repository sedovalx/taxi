package t

import play.api.libs.json.JsValue
import play.api.mvc.{Action, Request}

import scala.concurrent.Future

/**
 * Created by ipopkov on 25/04/15.
 */
trait EntityController {
   protected def create[A](request: Request[JsValue], creatorId: Int): Future[Action[A]]
   protected def read[A]: Future[Action[A]]
   protected def update[A](request: Request[JsValue], editorId: Int): Future[Action[A]]
   protected def delete[A](id: Int): Future[Action[A]]
 }
