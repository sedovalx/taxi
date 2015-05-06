package utils.extensions

import java.sql.Timestamp
import scala.language.implicitConversions
import org.joda.time.DateTime

object DateUtils {
  def now = new Timestamp(new java.util.Date().getTime)

  implicit def stringToDate(str: Option[String]) : Option[Timestamp] = {
    str match {
      case Some(s: String) => Some(new Timestamp(DateTime.parse(s).getMillis))
      case None => None
    }
  }
}

