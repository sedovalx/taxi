package utils

/**
 * Created by ipopkov on 05/04/15.
 */
import play.api.db.slick.Config.driver.simple._

package object db {

  // http://davidruescas.com/2013/12/27/querying-in-slick-with-many-optional-constraints/
  case class MaybeFilter[X, Y](query: scala.slick.lifted.Query[X, Y, Seq]) {
    def filteredBy(op: Option[_])(f:(X) => Column[Option[Boolean]]) = {
      op map { o => MaybeFilter(query.filter(f)) } getOrElse { this }
    }
  }

}
