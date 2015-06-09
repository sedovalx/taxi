package utils

import java.sql.Timestamp
import java.util.UUID

import models.entities.AccountType.AccountType
import models.entities.AccountType.AccountType
import models.generated.Tables._
import models.entities.{RentStatus => RS, AccountType}
import repository._
import repository.db.DbAccessor
import scaldi.{Injectable, Injector}
import utils.extensions.DateUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import play.api.db.slick.Config.driver.simple._

class TestModelGenerator(implicit val injector: Injector) extends Injectable with DbAccessor{
  def generate() = Future {
    withDb { implicit session => session.withTransaction {
      deleteAll
      generateDrivers(20, Timestamp.valueOf("2015-04-28 00:00:00"))
      generateCars(50, Timestamp.valueOf("2015-04-03 00:00:00"))
      generateRents(18, 44, Timestamp.valueOf("2015-05-15 00:00:00"))
      generateRentStates(60)
      generateBalanceChanges
    }}
  }

  def deleteAll(implicit session: Session): Unit = {
    OperationTable.delete.run
    RentStatusTable.delete.run
    RentTable.delete.run
    DriverTable.delete.run
    CarTable.delete.run
  }

  def generateBalanceChanges(implicit session: Session): Unit = {
    import repository.db.MappedColumnTypes._
    val rents = RentStatusTable.groupBy(s => s.rentId).map { case (rentId, group) =>
      val startTime = group.map(s => s.changeTime).min
      val endTime = group.map(s => s.changeTime).max
      val isClosed = group.map { s => Case If(s.status === RS.Closed) Then 1 Else 0 }.sum > 0
      (rentId, startTime, endTime, isClosed)
    }.run

    rents.map { i =>
      (
        i._1,
        i._2.get,
        i._3.get,
        i._4 match { case Some(x) if x => true case _ => false }
      )
    }.foreach { r =>
      val changesCount = Random.nextInt(20) + 2
      (0 to changesCount).foreach { _ =>
        Random.nextInt(5) match {
          case 0 | 1 | 2 =>
            createPayment(r._1, r._2, if (r._4) r._3 else DateUtils.now)
          case 3 =>
            createFine(r._1, r._2, if (r._4) r._3 else DateUtils.now)
          case 4 =>
            createRepair(r._1, r._2, if (r._4) r._3 else DateUtils.now)
        }
      }
    }
  }

  private def createOperation(accountType: AccountType, rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    val creationTime = getRandomDateBetween(startTime, endTime)
    val repo = inject [OperationRepo]
    repo.create(Operation(
      id = 0,
      accountType = accountType,
      rentId = rentId,
      changeTime = creationTime,
      amount = BigDecimal(Random.nextDouble()*4000 + 1000),
      creationDate = Some(creationTime),
      presence = if (Random.nextFloat() < 0.7) true else false
    ))
  }

  private def createPayment(rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    createOperation(AccountType.Rent, rentId, startTime, endTime)
  }

  private def createFine(rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    createOperation(AccountType.Fine, rentId, startTime, endTime)
  }

  private def createRepair(rentId: Int, startTime: Timestamp, endTime: Timestamp)(implicit session: Session): Unit = {
    createOperation(AccountType.Repair, rentId, startTime, endTime)
  }

  def generateRentStates(closedPercent: Int)(implicit session: Session): Unit = {
    RentTable.map(r => (r.id, r.creationDate)).run.foreach { i =>
      addGeneralStates(i._1, i._2.get)
      if (isClosed(closedPercent)){
        addFinalStates(i._1, i._2.get)
      }
    }
  }

  private def addGeneralStates(rentId: Int, startTime: Timestamp)(implicit session: Session) = {
    val statesCount = Random.nextInt(5) + 1
    var times = List[Timestamp]()
    (0 to statesCount).foldLeft(startTime){ (t, n) =>
      times :+= t
      val endTime = getDateBetween(times(n), DateUtils.now, 0.5f)
      getRandomDateBetween(times(n), endTime)
    }
    val statuses = times.indices.map { i =>
      RentStatus(
        id = 0,
        changeTime = times(i),
        status = if (i % 2 == 0) RS.Active else RS.Suspended,
        rentId = rentId,
        creationDate = Some(times(i)))
    }

    val repo = inject [RentStatusRepo]
    statuses.foreach { s => repo.create(s) }
  }

  private def addFinalStates(rentId: Int, startTime: Timestamp)(implicit session: Session) = {
    val lastStatus = RentStatusTable.filter { s => s.rentId === rentId }.sortBy(s => s.changeTime.desc).run.head
    val startDate = lastStatus.changeTime
    val settlingTime = getRandomDateBetween(startDate, DateUtils.now)
    val closedTime = getRandomDateBetween(settlingTime, DateUtils.now)
    val repo = inject [RentStatusRepo]
    repo.create(RentStatus(
      id = 0,
      rentId = rentId,
      changeTime = settlingTime,
      status = RS.SettlingUp,
      creationDate = Some(settlingTime)
    ))
    repo.create(RentStatus(
      id = 0,
      rentId = rentId,
      changeTime = closedTime,
      status = RS.Closed,
      creationDate = Some(closedTime)
    ))
  }

  private def isClosed(closedPercent: Int): Boolean = {
    Random.nextInt(100) <= closedPercent
  }

  def generateRents(drivers: Int, cars: Int, endDate: Timestamp)(implicit session: Session): Unit = {
    val rentRepo = inject [RentRepo]
    val data = (for {
      car <- CarTable.sortBy(c => c.id*Random.nextInt(100)).take(cars)
      driver <- DriverTable.sortBy(d => d.id*Random.nextInt(100)).take(drivers)
    } yield (driver.id, car.id, driver.creationDate, car.creationDate)).run

    data.foreach { d =>
      val startDate = getMaxDate(d._3, d._4)
      val date = getRandomDateBetween(startDate, endDate)
      val rent = Rent(
        id = 0,
        driverId = d._1,
        carId = d._2,
        deposit = Random.nextInt(5000) + 1000,
        creationDate = Some(date)
      )
      rentRepo.create(rent)
    }
  }

  private def getMaxDate(dates: Option[Timestamp]*): Timestamp = {
    val t = dates.map { d => d.get.getTime }.max
    new Timestamp(t)
  }

  private def getDateBetween(startTime: Timestamp, endTime: Timestamp, k: Float) = {
    val start = startTime.getTime
    val end = endTime.getTime

    new Timestamp(start + (k*(end - start)).toLong)
  }

  private def getRandomDateBetween(startTime: Timestamp, endTime: Timestamp) =
    getDateBetween(startTime, endTime, Random.nextFloat())

  def generateDrivers(count: Int, onDate: Timestamp)(implicit session: Session): Unit = {
    val names = List("Абель", "Абдулла", "Агдалия", "Агзам", "Аглия", "Аглям", "Агиля", "Агния", "Аделина", "Адип", "Азат", "Азалия", "Азаль", "Азамат", "Азхар", "Азиз", "Азим", "Аида", "Айбану", "Айбат", "Айгуль", "Айдар", "Багида", "Багман", "Бадира", "Бакир", "Бакира", "Банат", "Бану", "Барс", "Батулла", "Бахадир", "Бахир", "Бахрам", "Бахтияр", "Баяз", "Баян", "Белла", "Беркут", "Бика", "Бикбай", "Бикбулат", "Бикбай", "Билал", "Болгар", "Булат", "Буранбай", "Бэхет", "Габбас", "Габит", "Габдулла", "Гадел", "Гаден", "Гази", "Газиз", "Газим", "Газия", "Гайша", "Гайфулла", "Гали", "Галим", "Галима", "Галимулла", "Галиулла", "Галия", "Гамил", "Гани", "Гариф", "Гата", "Гафар", "Гафият", "Гаяз", "Гаян", "Гузель", "Гуль", "Гульзар", "Гульназ", "Гульнара", "Гульнур", "Гульчечек", "Гусман", "Гэрэй", "Забир", "Зайд", "Зайнаб", "Зайнулла", "Зайтуна", "Закария", "Закир", "Заки", "Залика", "Залия", "Замам", "Заман", "Замир", "Замира", "Зариф", "Захид", "Захир", "Зиля", "Зиннат", "Зиннур", "Зифа", "Зия", "Зульфат", "Зульфия", "Зуфар", "Зухра", "Зыятдин", "Ильфат", "Ильшат", "Ильяс", "Ильгам", "Иман", "Индира", "Инсаф", "Ирада", "Ирек", "Ирина", "Иса", "Искандер", "Ислам", "Исмаил", "Исмат", "Исфандияр", "Исхак", "Камал", "Камалетдин", "Камария", "Камиль", "Карим", "Касим", "Катиба", "Кафил", "Кашфулла", "Каюм", "Кахир", "Кирам", "Клара", "Кулахмет", "Курбан", "Латифа", "Лениза", "Ленора", "Ленур", "Лея", "Лиана", "Локман", "Луиза")
    val surnames = List("Абаимов", "Абакумов", "Абакшин", "Абалакин", "Абалаков", "Абалдуев", "Абалкин", "Абатурин", "Абатуров", "Абашев", "Абашин", "Абашкин", "Абаянцев", "Абдула", "Абдулин", "Абдулов", "Абоимов", "Абраменко", "Абрамкин", "Абрамов", "Абрамович", "Абрамцев", "Абрамчук", "Абрамычев", "Абрашин", "Бабаджанов", "Бабаев", "Бабакин", "Бабаков", "Бабанин", "Бабанов", "Бабарыкин", "Бабарыко", "Бабахин", "Бабаченко", "Бабенин", "Бабенко", "Бабёнышев", "Бабий", "Бабиков", "Бабин", "Бабинов", "Бабицын", "Бабич", "Бабичев", "Бабкин", "Баборыко", "Бабулин", "Бабунин", "Бабурин", "Бабухин", "Бабушкин", "Бабыкин", "Бавин", "Бавыкин", "Багаев", "Багин", "Багинин", "Баглаев", "Багреев", "Багримов", "Багров", "Вавилин", "Вавилов", "Вага", "Ваганков", "Ваганов", "Ваганьков", "Вагин", "Вагрин", "Вадбальский", "Вадбольский", "Вадимов", "Вадьяев", "Важенин", "Важин", "Вайванцев", "Вайгачев", "Вайтович", "Вакорев", "Вакорин", "Вакуленко", "Вакулин", "Вакулов", "Валахов", "Валдавин", "Валеев", "Валентинов", "Валенцов", "Валерианов", "Валерьев", "Валерьянов", "Евгеев", "Евгенов", "Евгеньев", "Евгранов", "Евграфов", "Евграшин", "Евдакимов", "Евдаков", "Евдокименко", "Евдокимов", "Евдонин", "Евдохин", "Евдошин", "Евклидов", "Евлампиев", "Евлампьев", "Евланин", "Евланов", "Евлахин", "Евлахов", "Евлашев", "Евлашин", "Евлашкин", "Евлашов", "Евлентьев", "Евлонин", "Евмененко", "Евменов", "Евментьев", "Евменьев", "Евпалов", "Евпатов", "Евпланов", "Евплов", "Евпсихеев", "Евреев", "Евреинов", "Евсеев", "Евсеенко", "Маврин", "Мавринский", "Мавроди", "Мавродиев", "Мавродий", "Мавродин", "Маврыкин", "Маврычев", "Магеркин", "Магеря", "Магницкий", "Магомедбеков", "Магомедов", "Магомедрасулов", "Магоня", "Магура", "Магуренко", "Мадаев", "Мадьяров", "Мажаров", "Мазанов", "Мазаньков", "Мазиков", "Мазилкин", "Мазилов", "Мазин", "Мазицын", "Мазко", "Мазнев", "Мазняк", "Мазовецкий", "Мазунин", "Обабков", "Обакумов", "Обакшин", "Обарин", "Обатуров", "Обаянцев", "Обезьянинов", "Обернибесов", "Оберучев", "Обиняков", "Обичкин", "Облонский", "Обнорский", "Обноскин", "Обносков", "Ободин", "Оболдуев", "Оболенский", "Оболенцев", "Оболонский", "Оборин", "Оботуров", "Обоянцев", "Образков", "Образский", "Образцов", "Обрезков", "Обреимов", "Обросимов", "Обросов", "Обручев", "Табаков", "Табачник", "Табачников", "Табашников", "Таболин", "Таболкин", "Табунщиков", "Таволжанский", "Таганов", "Таганцев", "Тагашов", "Тагильцев", "Тагиров", "Таиров", "Такмаков", "Талабанов", "Талаболин", "Талагаев", "Талалаев", "Талалакин", "Талалахин", "Талалихин", "Талалыкин", "Таланин", "Таланкин", "Таланов", "Талантов", "Талашин", "Талдонин", "Талдыкин", "Талимонов", "Талипов", "Талицкий", "Таловеров", "Талызин")
    val repo = inject [DriverRepo]
    (0 to count).foreach { _ =>
      val driver = Driver(
        id = 0,
        lastName = surnames(Random.nextInt(surnames.size)),
        firstName = names(Random.nextInt(names.size)),
        pass = s"${randomNumbersString(2)} ${randomNumbersString(2)} ${randomNumbersString(6)}",
        license = s"${randomNumbersString(2)} ${randomNumbersString(2)} ${randomNumbersString(6)}",
        phone = s"+7(9${randomNumbersString(2)}) ${randomNumbersString(3)}-${randomNumbersString(2)}-${randomNumbersString(2)}",
        secPhone = s"(4${randomNumbersString(2)}) ${randomNumbersString(3)}-${randomNumbersString(2)}-${randomNumbersString(2)}",
        address = UUID.randomUUID().toString,
        creationDate = Some(onDate)
      )
      repo.create(driver)
    }
  }

  private def randomNumbersString(length: Int): String = {
    (0 until length).foldLeft("")((agg, _) => agg + Random.nextInt(10).toString)
  }

  def generateCars(count: Int, onDate: Timestamp)(implicit session: Session): Unit = {
    val numberLetters = List("E", "T", "O", "P", "A", "H", "K", "X", "C", "B", "M")
    val cars = Map(
      "Renault" -> List(
        "Logan",
        "Megane",
        "Fluence",
        "Sandero"
      ),
      "Toyota" -> List(
        "Land Cruiser",
        "Camry",
        "Corolla",
        "RAV4"
      ),
      "Hundai" -> List(
        "Solaris",
        "Elantra",
        "ix35",
        "i35"
      ),
      "KIA" -> List(
        "Cee'd",
        "Cerato",
        "Optima",
        "Rio"
      ),
      "Mitsubishi" -> List(
        "Lancer",
        "Pajero",
        "Outlander",
        "ASX"
      )
    )

    def randomMake() = cars.keys.toList(Random.nextInt(cars.size))

    def randomModel(make: String) = cars(make)(Random.nextInt(cars(make).size))

    def randomLetters(length: Int): String = (0 until length).foldLeft("")((agg, _) => agg + numberLetters(Random.nextInt(numberLetters.size)))

    val repo = inject [CarRepo]
    (0 to count).foreach { _ =>
      val make = randomMake()
      val mileage = Random.nextInt(100000) + 3000
      val car = Car(
        id = 0,
        regNumber = s"${randomLetters(1)}${randomNumbersString(3)}${randomLetters(2)}${randomNumbersString(3)}",
        make = make,
        model = randomModel(make),
        rate = Random.nextInt(5000) + 1000,
        mileage = mileage,
        service = if (Random.nextFloat() < 0.7) Some(mileage match {
          case x if x <= 20000 => 20000
          case x if x > 20000 && x <= 45000 => 45000
          case x if x > 45000 && x <= 90000 => 90000
          case _ => 110000
        }) else None,
        creationDate = Some(onDate)
      )
      repo.create(car)
    }
  }
}
