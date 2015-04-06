package utils

/**
 * Created by ipopkov on 05/04/15.
 */
import play.api.db.slick.Config.driver.simple._

package object db {

  case class MaybeFilter[X, Y](val query: scala.slick.lifted.Query[X, Y, Seq]) {
    def filteredBy(op: Option[_])(f:(X) => Column[Option[Boolean]]) = {
      op map { o => MaybeFilter(query.filter(f)) } getOrElse { this }
    }
  }

}
