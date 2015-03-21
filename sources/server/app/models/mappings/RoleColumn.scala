package models.mappings

import models.entities.Role
import models.entities.Role._
import play.api.db.slick.Config.driver.simple._

object RoleColumn {
  implicit val roleColumnType = MappedColumnType.base[Role, String]( { r => r.toString }, { s => Role.withName(s) } )
}
