package utils.extensions

import java.sql.Date

object SqlDate {
  def now = new Date(new java.util.Date().getTime)
}
