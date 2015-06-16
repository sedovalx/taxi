import slick.driver.PostgresDriver.api._

package object repository {

  // http://davidruescas.com/2013/12/27/querying-in-slick-with-many-optional-constraints/
  case class MaybeFilter[X, Y](query: slick.lifted.Query[X, Y, Seq]) {
    def filteredBy(op: Option[_])(f:(X) => Rep[Option[Boolean]]): MaybeFilter[X, Y] = {
      op map { o => MaybeFilter(query.filter(f)) } getOrElse { this }
    }
  }

}
