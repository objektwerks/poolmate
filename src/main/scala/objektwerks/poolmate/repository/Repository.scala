package objektwerks.poolmate.repository

import java.sql.Date
import java.time.LocalDate

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

  implicit val dateMapper = MappedColumnType.base[LocalDate, Date](ld => Date.valueOf(ld),d => d.toLocalDate)
  val schema = owners.schema ++ pools.schema ++ cleanings.schema ++ measurements.schema ++ chemicals.schema ++ additives.schema ++ repairs.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def close() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  class Owners(tag: Tag) extends Table[Owner](tag, "owners") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")
    def street = column[String]("street")
    def city = column[String]("city")
    def state = column[String]("state")
    def zip = column[Int]("zip")
    def * = (id, name, email, street, city, state, zip) <> (Owner.tupled, Owner.unapply)
  }
  object owners extends TableQuery(new Owners(_)) {
    val compiledList = Compiled { sortBy(_.name.asc) }
    def save(owner: Owner) = (this returning this.map(_.id)).insertOrUpdate(owner)
    def list() = compiledList.result
  }

  class Pools(tag: Tag) extends Table[Pool](tag, "pools") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def ownerid = column[Int]("owner_id")
    def gallons = column[Double]("gallons")
    def * = (id, ownerid, gallons) <> (Pool.tupled, Pool.unapply)
    def ownerFk = foreignKey("owner_fk", ownerid, TableQuery[Owners])(_.id)
  }
  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled { sortBy(_.gallons.asc) }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result
  }

  class Cleanings(tag: Tag) extends Table[Cleaning](tag, "cleanings") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolid = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def deck = column[Boolean]("deck")
    def brush = column[Boolean]("brush")
    def net = column[Boolean]("net")
    def vacuum = column[Boolean]("vacuum")
    def skimmerBasket = column[Boolean]("skimmer_basket")
    def pumpBasket = column[Boolean]("pump_basket")
    def pumpFilter = column[Boolean]("pump_filter")
    def * = (id, poolid, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter) <> (Cleaning.tupled, Cleaning.unapply)
    def poolFk = foreignKey("pool_cleaning_fk", poolid, TableQuery[Pools])(_.id)
  }
  object cleanings extends TableQuery(new Cleanings(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolid === poolid).sortBy(_.on.asc) }
    def save(cleaning: Cleaning) = (this returning this.map(_.id)).insertOrUpdate(cleaning)
    def list(poolid: Int) = compiledList(poolid).result
  }

  class Measurements(tag: Tag) extends Table[Measurement](tag, "measurements") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolid = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def temp = column[Int]("temp")
    def totalHardness = column[Int]("total_hardness")
    def totalChlorine = column[Int]("total_chlorine")
    def totalBromine = column[Int]("total_bromine")
    def freeChlorine = column[Int]("free_chlorine")
    def pH = column[Double]("ph")
    def totalAlkalinity = column[Int]("total_alkalinity")
    def cyanuricAcid = column[Int]("cyanuric_acid")
    def * = (id, poolid, on, temp, totalHardness, totalChlorine, totalBromine, freeChlorine, pH, totalAlkalinity, cyanuricAcid) <> (Measurement.tupled, Measurement.unapply)
    def poolFk = foreignKey("pool_measurement_fk", poolid, TableQuery[Pools])(_.id)
  }
  object measurements extends TableQuery(new Measurements(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolid === poolid).sortBy(_.on.asc) }
    def save(measurement: Measurement) = (this returning this.map(_.id)).insertOrUpdate(measurement)
    def list(poolid: Int) = compiledList(poolid).result
  }

  class Chemicals(tag: Tag) extends Table[Chemical](tag, "chemicals") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def unit = column[String]("unit")
    def * = (id, name, unit) <> (Chemical.tupled, Chemical.unapply)
  }
  object chemicals extends TableQuery(new Chemicals(_)) {
    val compiledList = Compiled { sortBy(_.name.asc) }
    def save(chemical: Chemical) = (this returning this.map(_.id)).insertOrUpdate(chemical)
    def list() = compiledList.result
  }

  class Additives(tag: Tag) extends Table[Additive](tag, "additives") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolid = column[Int]("pool_id")
    def chemicalid = column[Int]("chemical_id")
    def on = column[LocalDate]("on")
    def amount = column[Double]("amount")
    def * = (id, poolid, chemicalid, on, amount) <> (Additive.tupled, Additive.unapply)
    def poolFk = foreignKey("pool_additive_fk", poolid, TableQuery[Pools])(_.id)
  }
  object additives extends TableQuery(new Additives(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolid === poolid).sortBy(_.on.asc) }
    def save(additive: Additive) = (this returning this.map(_.id)).insertOrUpdate(additive)
    def list(poolid: Int) = compiledList(poolid).result
  }

  class Repairs(tag: Tag) extends Table[Repair](tag, "repairs") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolid = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def cost = column[Double]("cost")
    def description = column[String]("description")
    def * = (id, poolid, on, cost, description) <> (Repair.tupled, Repair.unapply)
    def poolFk = foreignKey("pool_repair_fk", poolid, TableQuery[Pools])(_.id)
  }
  object repairs extends TableQuery(new Repairs(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolid === poolid).sortBy(_.on.asc) }
    def save(repair: Repair) = (this returning this.map(_.id)).insertOrUpdate(repair)
    def list(poolid: Int) = compiledList(poolid).result
  }
}