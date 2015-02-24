import java.sql.BatchUpdateException
import models.{Cars, CarClasses}
import utils.db.SchemaBuilder
import scala.slick.driver.PostgresDriver.simple._

// это выражение напечатает DDL-скрипт для создания таблиц в БД
// его нужно скопировать и выполнить в pgAdmin
// или же можно стартовать все приложение, таблицы будут созданы автоматически
println(SchemaBuilder.getDdlScript)

// создаем соединение с БД
// на локальной машине, на порту 5433 должен висеть сервер PostgreSql
// в котором создана БД с именем taxi
// для пользователя postgres на сервере должен быть задан пароль 11111
val database = Database.forURL(
  url = "jdbc:postgresql://localhost:5433/taxi",
  user = "postgres",
  password = "11111",
  prop = null,
  driver = "org.postgresql.Driver"
)

// дальнейший код тут сработает, если есть БД и таблицы в ней

// данные, которые вставляем в БД
val data = Seq(
  ("A-class", Seq("A345ET197", "B344KO13")),
  ("B-class", Seq("H233PO34", "K956EE23", "C123MP34")),
  ("C-class", Seq("M453KO777"))
)

// получаем сессию и выполняем команды
database withSession { implicit session =>
  try {
    // это же тест, поэтому сначала удаляем имеющиеся данные в БД
    // удаляем все машины
    Cars.objects.delete
    // удаляем все классы машин
    CarClasses.objects.delete
    // добавляем классы машин из данных выше
    // хитрая штука data.map(_._1):_* берет из каждого элемента data первую часть 
    // и преобразует к типу String*
    CarClasses.objects.map(_.name).insertAll(data.map(_._1):_*)
    // выполняется для каждого добавленного класса машины
    CarClasses.objects.foreach(/*тут с - это класс машины*/c => {
      // вставляем в таблицу car новые строки
      // при этом class_id берем из переменной c
      // а регномера из соответствующих номеров в коллекции data
      Cars.objects
        .map(r => (r.regNumber, r.classId))
        .insertAll(
          data.filter(i => i._1 == c.name).flatMap(_._2).map(n => (n, c.id)):_*
        )
    })
  } catch {
      case e: BatchUpdateException =>
        println(e)
        println(e.getNextException)
  }
}