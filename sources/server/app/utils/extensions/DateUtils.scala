package utils.extensions

import java.sql.Date

import org.joda.time.DateTime

object DateUtils {
  def now = new Date(new java.util.Date().getTime)

  implicit def stringToDate(str: Option[String]) : Option[Date] = {
    str match {
      case Some(s: String) => Some(new Date(DateTime.parse(s).getMillis))
      case None => None
    }
  }
}

