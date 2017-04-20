package objektwerks.poolmate.repository

import java.sql.{Date, Time}
import java.time.{LocalDate, LocalTime}

import com.typesafe.config.ConfigFactory
import objektwerks.poolmate.entity._
import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Repository {
  def newInstance(configFile: String): Repository = {
    val repository = new Repository(config = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load(configFile)), profile = H2Profile)
    import repository._
    try { await(pools.list()).length } catch { case _: Throwable => repository.createSchema() }
    repository
  }
}

class Repository(val config: DatabaseConfig[JdbcProfile], val profile: JdbcProfile, val awaitDuration: Duration = 1 second) {
  import profile.api._

  implicit val timeMapper = MappedColumnType.base[LocalTime, Time](lt => Time.valueOf(lt), t => t.toLocalTime)
  implicit val dateMapper = MappedColumnType.base[LocalDate, Date](ld => Date.valueOf(ld), d => d.toLocalDate)
  val schema = pools.schema ++ owners.schema ++ surfaces.schema ++ pumps.schema ++ timers.schema ++ heaters.schema ++
               lifecycles.schema ++ cleanings.schema ++ measurements.schema ++ additives.schema ++ repairs.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def close() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  class Pools(tag: Tag) extends Table[Pool](tag, "pools") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def built = column[LocalDate]("built")
    def gallons = column[Double]("gallons")
    def street = column[String]("street")
    def city = column[String]("city")
    def state = column[String]("state")
    def zip = column[Int]("zip")
    def * = (id, built, gallons, street, city, state, zip) <> (Pool.tupled, Pool.unapply)
  }
  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled { sortBy(p => (p.zip.asc, p.city.asc)) }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result
  }

  class Owners(tag: Tag) extends Table[Owner](tag, "owners") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def since = column[LocalDate]("since")
    def first = column[String]("first")
    def last = column[String]("last")
    def email = column[String]("email")
    def * = (id, poolId, since, first, last, email) <> (Owner.tupled, Owner.unapply)
    def poolFk = foreignKey("pool_owner_fk", poolId, TableQuery[Pools])(_.id)
  }
  object owners extends TableQuery(new Owners(_)) {
    val compiledList = Compiled { sortBy(o => (o.since.asc, o.last.asc)) }
    def save(owner: Owner) = (this returning this.map(_.id)).insertOrUpdate(owner)
    def list() = compiledList.result
  }

  class Surfaces(tag: Tag) extends Table[Surface](tag, "surfaces") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[LocalDate]("installed")
    def kind = column[String]("kind")
    def * = (id, poolId, installed, kind) <> (Surface.tupled, Surface.unapply)
    def poolFk = foreignKey("pool_surface_fk", poolId, TableQuery[Pools])(_.id)
  }
  object surfaces extends TableQuery(new Surfaces(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.asc) }
    def save(surface: Surface) = (this returning this.map(_.id)).insertOrUpdate(surface)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Pumps(tag: Tag) extends Table[Pump](tag, "pumps") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[LocalDate]("installed")
    def model = column[String]("model")
    def * = (id, poolId, installed, model) <> (Pump.tupled, Pump.unapply)
    def poolFk = foreignKey("pool_pump_fk", poolId, TableQuery[Pools])(_.id)
  }
  object pumps extends TableQuery(new Pumps(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.asc) }
    def save(pump: Pump) = (this returning this.map(_.id)).insertOrUpdate(pump)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Timers(tag: Tag) extends Table[Timer](tag, "timers") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[LocalDate]("installed")
    def model = column[String]("model")
    def * = (id, poolId, installed, model) <> (Timer.tupled, Timer.unapply)
    def poolFk = foreignKey("pool_timer_fk", poolId, TableQuery[Pools])(_.id)
  }
  object timers extends TableQuery(new Timers(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.asc) }
    def save(timer: Timer) = (this returning this.map(_.id)).insertOrUpdate(timer)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Heaters(tag: Tag) extends Table[Heater](tag, "heaters") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[LocalDate]("installed")
    def model = column[String]("model")
    def * = (id, poolId, installed, model) <> (Heater.tupled, Heater.unapply)
    def poolFk = foreignKey("pool_heater_fk", poolId, TableQuery[Pools])(_.id)
  }
  object heaters extends TableQuery(new Heaters(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.asc) }
    def save(heater: Heater) = (this returning this.map(_.id)).insertOrUpdate(heater)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Lifecycles(tag: Tag) extends Table[Lifecycle](tag, "lifecycles") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def created = column[LocalDate]("created")
    def active = column[Boolean]("active")
    def pumpOn = column[LocalTime]("pump_on")
    def pumpOff = column[LocalTime]("pump_off")
    def * = (id, poolId, created, active, pumpOn, pumpOff) <> (Lifecycle.tupled, Lifecycle.unapply)
    def poolFk = foreignKey("pool_lifecycle_fk", poolId, TableQuery[Pools])(_.id)
  }
  object lifecycles extends TableQuery(new Lifecycles(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(l => (l.active.asc, l.created.asc)) }
    def save(lifecycle: Lifecycle) = (this returning this.map(_.id)).insertOrUpdate(lifecycle)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Cleanings(tag: Tag) extends Table[Cleaning](tag, "cleanings") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def deck = column[Boolean]("deck")
    def brush = column[Boolean]("brush")
    def net = column[Boolean]("net")
    def vacuum = column[Boolean]("vacuum")
    def skimmerBasket = column[Boolean]("skimmer_basket")
    def pumpBasket = column[Boolean]("pump_basket")
    def pumpFilter = column[Boolean]("pump_filter")
    def * = (id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter) <> (Cleaning.tupled, Cleaning.unapply)
    def poolFk = foreignKey("pool_cleaning_fk", poolId, TableQuery[Pools])(_.id)
  }
  object cleanings extends TableQuery(new Cleanings(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolId === poolid).sortBy(_.on.asc) }
    def save(cleaning: Cleaning) = (this returning this.map(_.id)).insertOrUpdate(cleaning)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Measurements(tag: Tag) extends Table[Measurement](tag, "measurements") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def temp = column[Int]("temp")
    def hardness = column[Int]("hardness")
    def totalChlorine = column[Int]("total_chlorine")
    def bromine = column[Int]("bromine")
    def freeChlorine = column[Int]("free_chlorine")
    def pH = column[Double]("ph")
    def alkalinity = column[Int]("alkalinity")
    def cyanuricAcid = column[Int]("cyanuric_acid")
    def * = (id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid) <> (Measurement.tupled, Measurement.unapply)
    def poolFk = foreignKey("pool_measurement_fk", poolId, TableQuery[Pools])(_.id)
  }
  object measurements extends TableQuery(new Measurements(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.on.asc) }
    def save(measurement: Measurement) = (this returning this.map(_.id)).insertOrUpdate(measurement)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Additives(tag: Tag) extends Table[Additive](tag, "additives") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def chemical = column[String]("chemical")
    def unit = column[String]("unit")
    def amount = column[Double]("amount")
    def * = (id, poolId, on, chemical, unit, amount) <> (Additive.tupled, Additive.unapply)
    def poolFk = foreignKey("pool_additive_fk", poolId, TableQuery[Pools])(_.id)
  }
  object additives extends TableQuery(new Additives(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.on.asc) }
    def save(additive: Additive) = (this returning this.map(_.id)).insertOrUpdate(additive)
    def list(poolId: Int) = compiledList(poolId).result
  }

  class Repairs(tag: Tag) extends Table[Repair](tag, "repairs") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def cost = column[Double]("cost")
    def description = column[String]("description")
    def * = (id, poolId, on, cost, description) <> (Repair.tupled, Repair.unapply)
    def poolFk = foreignKey("pool_repair_fk", poolId, TableQuery[Pools])(_.id)
  }
  object repairs extends TableQuery(new Repairs(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.on.asc) }
    def save(repair: Repair) = (this returning this.map(_.id)).insertOrUpdate(repair)
    def list(poolId: Int) = compiledList(poolId).result
  }
}