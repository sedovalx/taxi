package utils.db
import play.api.db.slick.Config.driver.simple._
import play.api.db.DB
import play.api.Play.current

trait DbAccessor {
  /**
   * Выполнение функции в контексте сессии к БД
   * @param f функция, требующая сессию
   * @tparam T тип возвращаемых данных
   * @return результат работы функции
   */
  protected def withDb[T](f: Session => T) = {
    // withSession проследит, чтобы сессия была закрыта в любом случае
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      f(session)
    }
  }

  /**
   * Выполнение действия, непребуюущего результата, в контексте сессии к БД
   * @param f действие
   */
  protected def withDbAction(f: Session => Unit) = {
    Database.forDataSource(DB.getDataSource()) withSession { session =>
      f(session)
    }
  }
}
