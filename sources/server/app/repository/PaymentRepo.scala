package repository

import models.generated.Tables.{PaymentFilter, Payment, PaymentTable}
import play.api.db.slick.Config.driver.simple._

trait PaymentRepo extends GenericCRUD[Payment, PaymentTable, PaymentFilter]

class PaymentRepoImpl extends PaymentRepo {
  override val tableQuery = PaymentTable

  /**
   * Вернуть отфильтрованных пользователей
   * @param session сессия к БД
   * @return список пользователей, попавших под фильтр
   */
  override def read(filter: Option[PaymentFilter])(implicit session: Session): List[Payment] = {
    filter match {
      case None => super.read()
      case Some(f) =>
        tableQuery
          .filteredBy(f.rentId)(_.rentId === f.rentId.get)
          .query.list
    }
  }
}


