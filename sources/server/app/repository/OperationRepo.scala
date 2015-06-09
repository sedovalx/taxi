package repository

import models.generated.Tables.{OperationFilter, Operation, OperationTable}
import play.api.db.slick.Config.driver.simple._

trait OperationRepo extends GenericCRUD[Operation, OperationTable, OperationFilter]

class OperationRepoImpl extends OperationRepo {
  override val tableQuery = OperationTable

  override def read(filter: Option[OperationFilter])(implicit session: Session): List[Operation] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        tableQuery
          .filteredBy(f.rentId)(_.rentId === f.rentId.get)
          .query.list
    }
  }
}


