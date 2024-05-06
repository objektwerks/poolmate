package poolmate

import java.sql.{Date, Time, Timestamp}
import java.time.{LocalDate, LocalDateTime, LocalTime}

import com.typesafe.config.Config

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

class Repository(val config: DatabaseConfig[JdbcProfile],
                 val profile: JdbcProfile, 
                 val awaitDuration: Duration = 1 second):
  import profile.api._

  val schema = pools.schema ++ owners.schema ++ surfaces.schema ++ pumps.schema ++ timers.schema ++ heaters.schema ++
    lifecycles.schema ++ cleanings.schema ++ measurements.schema ++ additives.schema ++ supplies.schema ++ repairs.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)

  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def close() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))

  def dropSchema() = await(DBIO.seq(schema.drop))

  class Pools(tag: Tag) extends Table[Pool](tag, "pools"):
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def built = column[String]("built")
    def gallons = column[Int]("gallons")
    def street = column[String]("street")
    def city = column[String]("city")
    def state = column[String]("state")
    def zip = column[Int]("zip")
    def * = (id.?, built, gallons, street, city, state, zip).mapTo[Pool]

  object pools extends TableQuery(new Pools(_)):
    val compiledList = Compiled {
      sortBy(p => (p.zip.asc, p.city.asc))
    }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result

  class Owners(tag: Tag) extends Table[Owner](tag, "owners") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def since = column[String]("since")
    def first = column[String]("first")
    def last = column[String]("last")
    def email = column[String]("email")
    def poolFk = foreignKey("pool_owner_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, since, first, last, email).mapTo[Owner]
  }

  object owners extends TableQuery(new Owners(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(o => (o.since.desc, o.last.asc)) }
    def save(owner: Owner) = (this returning this.map(_.id)).insertOrUpdate(owner)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Surfaces(tag: Tag) extends Table[Surface](tag, "surfaces") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[String]("installed")
    def kind = column[String]("kind")
    def poolFk = foreignKey("pool_surface_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, installed, kind).mapTo[Surface]
  }

  object surfaces extends TableQuery(new Surfaces(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(surface: Surface) = (this returning this.map(_.id)).insertOrUpdate(surface)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Pumps(tag: Tag) extends Table[Pump](tag, "pumps") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[String]("installed")
    def model = column[String]("model")
    def poolFk = foreignKey("pool_pump_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, installed, model).mapTo[Pump]
  }

  object pumps extends TableQuery(new Pumps(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(pump: Pump) = (this returning this.map(_.id)).insertOrUpdate(pump)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Timers(tag: Tag) extends Table[Timer](tag, "timers") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[String]("installed")
    def model = column[String]("model")
    def poolFk = foreignKey("pool_timer_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, installed, model).mapTo[Timer]
  }

  object timers extends TableQuery(new Timers(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(timer: Timer) = (this returning this.map(_.id)).insertOrUpdate(timer)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Heaters(tag: Tag) extends Table[Heater](tag, "heaters") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[String]("installed")
    def model = column[String]("model")
    def poolFk = foreignKey("pool_heater_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, installed, model).mapTo[Heater]
  }

  object heaters extends TableQuery(new Heaters(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(heater: Heater) = (this returning this.map(_.id)).insertOrUpdate(heater)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Lifecycles(tag: Tag) extends Table[Lifecycle](tag, "lifecycles") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def created = column[String]("created")
    def active = column[Boolean]("active")
    def pumpOn = column[String]("pump_on")
    def pumpOff = column[String]("pump_off")
    def poolFk = foreignKey("pool_lifecycle_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, created, active, pumpOn, pumpOff).mapTo[Lifecycle]
  }

  object lifecycles extends TableQuery(new Lifecycles(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(l => (l.active.asc, l.created.desc)) }
    def save(lifecycle: Lifecycle) = (this returning this.map(_.id)).insertOrUpdate(lifecycle)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Cleanings(tag: Tag) extends Table[Cleaning](tag, "cleanings") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[String]("on")
    def deck = column[Boolean]("deck")
    def brush = column[Boolean]("brush")
    def net = column[Boolean]("net")
    def vacuum = column[Boolean]("vacuum")
    def skimmerBasket = column[Boolean]("skimmer_basket")
    def pumpBasket = column[Boolean]("pump_basket")
    def pumpFilter = column[Boolean]("pump_filter")
    def poolFk = foreignKey("pool_cleaning_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter).mapTo[Cleaning]
  }

  object cleanings extends TableQuery(new Cleanings(_)) {
    val compiledList = Compiled { ( poolid: Rep[Int] ) => filter(_.poolId === poolid).sortBy(_.on.desc) }
    def save(cleaning: Cleaning) = (this returning this.map(_.id)).insertOrUpdate(cleaning)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Measurements(tag: Tag) extends Table[Measurement](tag, "measurements") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[String]("on")
    def temp = column[Double]("temp")
    def hardness = column[Double]("hardness")
    def totalChlorine = column[Double]("total_chlorine")
    def bromine = column[Double]("bromine")
    def freeChlorine = column[Double]("free_chlorine")
    def pH = column[Double]("ph")
    def alkalinity = column[Double]("alkalinity")
    def cyanuricAcid = column[Double]("cyanuric_acid")
    def poolFk = foreignKey("pool_measurement_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid).mapTo[Measurement]
  }

  object measurements extends TableQuery(new Measurements(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.on.desc) }
    def save(measurement: Measurement) = (this returning this.map(_.id)).insertOrUpdate(measurement)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Additives(tag: Tag) extends Table[Additive](tag, "additives") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[String]("on")
    def chemical = column[String]("chemical")
    def unit = column[String]("unit")
    def amount = column[Double]("amount")
    def poolFk = foreignKey("pool_additive_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, on, chemical, unit, amount).mapTo[Additive]
  }

  object additives extends TableQuery(new Additives(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.on.desc) }
    def save(additive: Additive) = (this returning this.map(_.id)).insertOrUpdate(additive)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Supplies(tag: Tag) extends Table[Supply](tag, "supplies") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def purchased = column[String]("purchased")
    def item = column[String]("item")
    def unit = column[String]("unit")
    def amount = column[Double]("amount")
    def cost = column[Double]("cost")
    def poolFk = foreignKey("pool_supply_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, purchased, item, unit, amount, cost).mapTo[Supply]
  }

  object supplies extends TableQuery(new Supplies(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.purchased.desc) }
    def save(supply: Supply) = (this returning this.map(_.id)).insertOrUpdate(supply)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Repairs(tag: Tag) extends Table[Repair](tag, "repairs") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[String]("on")
    def item = column[String]("item")
    def cost = column[Double]("cost")
    def poolFk = foreignKey("pool_repair_fk", poolId, TableQuery[Pools])(_.id)
    def * = (id.?, poolId, on, item, cost).mapTo[Repair]
  }

  object repairs extends TableQuery(new Repairs(_)) {
    val compiledList = Compiled { ( poolId: Rep[Int] ) => filter(_.poolId === poolId).sortBy(_.on.desc) }
    def save(repair: Repair) = (this returning this.map(_.id)).insertOrUpdate(repair)
    def list(poolId: Int) = compiledList(poolId).result
  }