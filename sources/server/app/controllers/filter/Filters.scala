package controllers.filter

import java.sql.Date

import models.entities.Role.Role

/**
 * Created by ipopkov on 04/04/15.
 */
case class UserFilter(
                       login: Option[String],
                       lastName: Option[String],
                       firstName: Option[String],
                       middleName: Option[String],
                       role: Option[Role],
                       creationDate: Option[Date]
                       )