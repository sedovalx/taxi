package repository.db

import models.entities.Role
import models.entities.Role.Role
import play.api.db.slick.Config.driver.simple._

object MappedColumnTypes {
  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )
}
