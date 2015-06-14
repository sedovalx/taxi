package repository

import models.generated.Tables
import models.generated.Tables.{OperationFilter, Operation, OperationTable}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future

class OperationRepo extends GenericCRUD[Operation, OperationTable, OperationFilter] {
  /**
   * Вернуть отфильтрованных пользователей
   * @return список пользователей, попавших под фильтр
   */
  override def read(filter: Option[Tables.OperationFilter]): Future[Seq[Tables.Operation]] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        db.run(tableQuery
          .filteredBy(f.rentId)(_.rentId.? === f.rentId).query.result)
    }
  }

  override val tableQuery: PostgresDriver.api.TableQuery[Tables.OperationTable] = OperationTable
}
