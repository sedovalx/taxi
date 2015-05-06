package controllers.filter

import java.sql.Timestamp
import models.entities.Role.Role


/**
 * Created by ipopkov on 04/04/15.
 */
case class AccountFilter(
                       login: Option[String],
                       lastName: Option[String],
                       firstName: Option[String],
                       middleName: Option[String],
                       role: Option[Role],
                       creationDate: Option[Timestamp]
                       )