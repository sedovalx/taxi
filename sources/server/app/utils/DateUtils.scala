package utils

import java.sql.Timestamp
import java.text.SimpleDateFormat

import scala.language.implicitConversions

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

