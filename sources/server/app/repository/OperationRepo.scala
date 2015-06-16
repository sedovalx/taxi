package repository

import models.generated.Tables
import models.generated.Tables.{OperationFilter, Operation, OperationTable}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import db.MappedColumnTypes._

import scala.concurrent.Future

class OperationRepo extends GenericCRUDImpl[Operation, OperationTable, OperationFilter] {
  /**
   * Вернуть отфильтрованных пользователей
   * @return список пользователей, попавших под фильтр
   */
  override def read(filter: Option[Tables.OperationFilter]): Future[Seq[Tables.Operation]] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        db.run(tableQuery
          .filteredBy(f.rentId)(_.rentId.? === f.rentId)
          .filteredBy(f.accountType)(_.accountType.? === f.accountType)
          .query.sortBy(o => o.changeTime).result)
    }
  }

  override val tableQuery: PostgresDriver.api.TableQuery[Tables.OperationTable] = OperationTable
}

