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
    try { await(owners.list()).length } catch { case _: Throwable => repository.createSchema() }
    repository
  }
}

class Repository(val config: DatabaseConfig[JdbcProfile], val profile: JdbcProfile, val awaitDuration: Duration = 1 second) {
  import profile.api._

  implicit val timeMapper = MappedColumnType.base[LocalTime, Time](lt => Time.valueOf(lt), t => t.toLocalTime)
  implicit val dateMapper = MappedColumnType.base[LocalDate, Date](ld => Date.valueOf(ld),d => d.toLocalDate)
  val schema = locations.schema ++ owners.schema ++ pools.schema ++ cleanings.schema ++ measurements.schema ++ additives.schema ++ repairs.schema ++ timers.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def close() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  class Locations(tag: Tag) extends Table[Location](tag, "locations") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def street = column[String]("street")
    def city = column[String]("city")
    def state = column[String]("state")
    def zip = column[Int]("zip")
    def * = (id, street, city, state, zip) <> (Location.tupled, Location.unapply)
  }
  object locations extends TableQuery(new Locations(_)) {
    val compiledList = Compiled { sortBy(l => (l.city.asc, l.state.asc)) }
    def save(location: Location) = (this returning this.map(_.id)).insertOrUpdate(location)
    def list() = compiledList.result
  }

  class Owners(tag: Tag) extends Table[Owner](tag, "owners") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def locationId = column[Int]("location_id")
    def poolId = column[Int]("pool_id")
    def first = column[String]("first")
    def last = column[String]("last")
    def email = column[String]("email")
    def * = (id, locationId, poolId, first, last, email) <> (Owner.tupled, Owner.unapply)
    def locationFk = foreignKey("owner_location_fk", locationId, TableQuery[Locations])(_.id)
    def poolFk = foreignKey("owner_pool_fk", poolId, TableQuery[Pools])(_.id)
  }
  object owners extends TableQuery(new Owners(_)) {
    val compiledList = Compiled { sortBy(_.last.asc) }
    def save(owner: Owner) = (this returning this.map(_.id)).insertOrUpdate(owner)
    def list() = compiledList.result
  }

  class Pools(tag: Tag) extends Table[Pool](tag, "pools") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def currentOwnerId = column[Int]("current_owner_id")
    def locationId = column[Int]("location_id")
    def gallons = column[Double]("gallons")
    def surface = column[String]("surface")
    def pump = column[String]("pump")
    def timer = column[String]("timer")
    def heater = column[String]("heater")
    def * = (id, currentOwnerId, locationId, gallons, surface, pump, timer, heater) <> (Pool.tupled, Pool.unapply)
    def ownerFk = foreignKey("pool_owner_fk", currentOwnerId, TableQuery[Owners])(_.id)
    def locationFk = foreignKey("pool_location_fk", locationId, TableQuery[Locations])(_.id)
  }
  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled { sortBy(_.gallons.asc) }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result
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
    def list(poolid: Int) = compiledList(poolid).result
  }

  class Measurements(tag: Tag) extends Table[Measurement](tag, "measurements") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def temp = column[Int]("temp")
    def totalHardness = column[Int]("total_hardness")
    def totalChlorine = column[Int]("total_chlorine")
    def totalBromine = column[Int]("total_bromine")
    def freeChlorine = column[Int]("free_chlorine")
    def pH = column[Double]("ph")
    def totalAlkalinity = column[Int]("total_alkalinity")
    def cyanuricAcid = column[Int]("cyanuric_acid")
    def * = (id, poolId, on, temp, totalHardness, totalChlorine, totalBromine, freeChlorine, pH, totalAlkalinity, cyanuricAcid) <> (Measurement.tupled, Measurement.unapply)
    def poolFk = foreignKey("pool_measurement_fk", poolId, TableQuery[Pools])(_.id)
  }
  object measurements extends TableQuery(new Measurements(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolId === poolid).sortBy(_.on.asc) }
    def save(measurement: Measurement) = (this returning this.map(_.id)).insertOrUpdate(measurement)
    def list(poolid: Int) = compiledList(poolid).result
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
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolId === poolid).sortBy(_.on.asc) }
    def save(additive: Additive) = (this returning this.map(_.id)).insertOrUpdate(additive)
    def list(poolid: Int) = compiledList(poolid).result
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
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolId === poolid).sortBy(_.on.asc) }
    def save(repair: Repair) = (this returning this.map(_.id)).insertOrUpdate(repair)
    def list(poolid: Int) = compiledList(poolid).result
  }

  class Timers(tag: Tag) extends Table[Timer](tag, "timers") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalTime]("on")
    def off = column[LocalTime]("off")
    def * = (id, poolId, on, off) <> (Timer.tupled, Timer.unapply)
    def poolFk = foreignKey("pool_timer_fk", poolId, TableQuery[Pools])(_.id)
  }
  object timers extends TableQuery(new Timers(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolId === poolid).sortBy(_.on.asc) }
    def save(timer: Timer) = (this returning this.map(_.id)).insertOrUpdate(timer)
    def list(poolid: Int) = compiledList(poolid).result
  }
}