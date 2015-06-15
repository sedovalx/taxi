package filters

import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.gzip.GzipFilter

class Filters @Inject() (gzipFilter: GzipFilter, accessFilter: HttpAccessFilter) extends HttpFilters {
  def filters = Seq(gzipFilter, accessFilter)
}
