package utils.extensions

import java.sql.Timestamp
import java.text.SimpleDateFormat
import scala.language.implicitConversions
import org.joda.time.DateTime

object DateUtils {

  val iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

  def now = new Timestamp(new java.util.Date().getTime)

  implicit def stringToDate(str: Option[String]) : Option[Timestamp] = {
    str match {
      case Some(s: String) => Some(new Timestamp(iso8601Format.parse(s).getTime))
      case None => None
    }
  }
}

