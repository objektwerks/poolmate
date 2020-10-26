package objektwerks.poolmate.repository

import java.sql.{Date, Time, Timestamp}
import java.time.{LocalDate, LocalDateTime, LocalTime}

import com.typesafe.config.ConfigFactory

import objektwerks.poolmate.entity._

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Repository {
  def apply(configFile: String): Repository = {
    val config = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load(configFile))
    val repository = new Repository(config, H2Profile)
    import repository._
    try {
      await(pools.list()).length
    } catch {
      case _: Throwable => repository.createSchema()
    }
    repository
  }
}

class Repository(val config: DatabaseConfig[JdbcProfile],
                 val profile: JdbcProfile, 
                 val awaitDuration: Duration = 1 second) {
  import profile.api._

  implicit val timeMapper = MappedColumnType.base[LocalTime, Time](lt => Time.valueOf(lt), t => t.toLocalTime)
  implicit val dateMapper = MappedColumnType.base[LocalDate, Date](ld => Date.valueOf(ld), d => d.toLocalDate)
  implicit val dateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](ldt => Timestamp.valueOf(ldt), ts => ts.toLocalDateTime)

  val db = config.db
  val schema = companies.schema ++ workers.schema ++ workOrders.schema ++ routeOrders.schema ++ locations.schema ++ routes.schema ++ stops.schema ++
    pools.schema ++ owners.schema ++ surfaces.schema ++ pumps.schema ++ timers.schema ++ heaters.schema ++ lifecycles.schema ++
    cleanings.schema ++ measurements.schema ++ additives.schema ++ supplies.schema ++ repairs.schema

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)
  def close() = db.close()
  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  class Companies(tag: Tag) extends Table[Company](tag, "companies") {
    def * = (id, name, since, website, email).<>(Company.tupled, Company.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def since = column[LocalDate]("since")
    def website = column[String]("website")
    def email = column[String]("email")
  }

  class Workers(tag: Tag) extends Table[Worker](tag, "workers") {
    def * = (id, companyId, hired, terminated, first, last, email).<>(Worker.tupled, Worker.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def hired = column[LocalDate]("hired")
    def terminated = column[Option[LocalDate]]("terminated")
    def first = column[String]("first")
    def last = column[String]("last")
    def email = column[String]("email")
    def companyFk = foreignKey("company_worker_fk", companyId, TableQuery[Pools])(_.id)
    def companyId = column[Int]("company_id")
  }

  class WorkOrders(tag: Tag) extends Table[WorkOrder](tag, "workorders") {
    def * = (id, poolId, description, created, completed).<>(WorkOrder.tupled, WorkOrder.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def description = column[String]("desciption")
    def created = column[LocalDate]("created")
    def completed = column[Option[LocalDateTime]]("completed")
    def poolFk = foreignKey("pool_work_order_fk", poolId, TableQuery[Pools])(_.id)
    def poolId = column[Int]("pool_id")
  }

  class WorkOrderWorkers(tag: Tag) extends Table[WorkOrderWorker](tag, "workorderworkers") {
    def * = (id, workOrderId, workerId).<>(WorkOrderWorker.tupled, WorkOrderWorker.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def workOrderId = column[Int]("work_order_id")
    def workerId = column[Int]("worker_id")
    def workOrderFk = foreignKey("work_order_work_order_worker_fk", workOrderId, TableQuery[WorkOrders])(_.id)
    def workerFk = foreignKey("worker_work_order_worker_fk", workerId, TableQuery[Workers])(_.id)
  }

  class RouteOrders(tag: Tag) extends Table[RouteOrder](tag, "routeorders") {
    def * = (id, routeId, created, completed, recurring).<>(RouteOrder.tupled, RouteOrder.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def routeId = column[Int]("route_id")
    def created = column[LocalDate]("created")
    def completed = column[LocalDate]("completed")
    def recurring = column[Boolean]("recurring")
    def routeFk = foreignKey("route_route_order_fk", routeId, TableQuery[Routes])(_.id)
  }

  class RouteOrderWorkers(tag: Tag) extends Table[RouteOrderWorker](tag, "routeorderworkers") {
    def * = (id, routeOrderId, workerId).<>(RouteOrderWorker.tupled, RouteOrderWorker.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def routeOrderId = column[Int]("route_order_id")
    def workerId = column[Int]("worker_id")
    def routeOrderFk = foreignKey("route_order_route_order_worker_fk", routeOrderId, TableQuery[RouteOrders])(_.id)
    def workerFk = foreignKey("worker_route_order_worker_fk", workerId, TableQuery[Workers])(_.id)
  }

  class Locations(tag: Tag) extends Table[Location](tag, "locations") {
    def * = (id, routeOrderId, poolId, ordinality, completed).<>(Location.tupled, Location.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def ordinality = column[Int]("ordinality")
    def completed = column[Option[LocalDateTime]]("completed")
    def routeOrderFk = foreignKey("route_order_location_fk", routeOrderId, TableQuery[RouteOrders])(_.id)
    def routeOrderId = column[Int]("route_order_id")
    def poolFk = foreignKey("pool_location_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Routes(tag: Tag) extends Table[Route](tag, "routes") {
    def * = (id, name).<>(Route.tupled, Route.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Unique)
  }

  class Stops(tag: Tag) extends Table[Stop](tag, "stops") {
    def * = (routeId, poolId, ordinality).<>(Stop.tupled, Stop.unapply)
    def poolId = column[Int]("pool_id")
    def ordinality = column[Int]("ordinality")
    def pk = primaryKey("pk", (routeId, poolId))
    def routeFk = foreignKey("route_stop_fk", routeId, TableQuery[Routes])(_.id)
    def routeId = column[Int]("route_id")
    def poolFk = foreignKey("pool_stop_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Pools(tag: Tag) extends Table[Pool](tag, "pools") {
    def * = (id, built, gallons, street, city, state, zip).<>(Pool.tupled, Pool.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def built = column[LocalDate]("built")
    def gallons = column[Int]("gallons")
    def street = column[String]("street")
    def city = column[String]("city")
    def state = column[String]("state")
    def zip = column[Int]("zip")
  }

  class Owners(tag: Tag) extends Table[Owner](tag, "owners") {
    def * = (id, poolId, since, first, last, email).<>(Owner.tupled, Owner.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def since = column[LocalDate]("since")
    def first = column[String]("first")
    def last = column[String]("last")
    def email = column[String]("email")
    def poolFk = foreignKey("pool_owner_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Surfaces(tag: Tag) extends Table[Surface](tag, "surfaces") {
    def * = (id, poolId, installed, kind).<>(Surface.tupled, Surface.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def installed = column[LocalDate]("installed")
    def kind = column[String]("kind")
    def poolFk = foreignKey("pool_surface_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Pumps(tag: Tag) extends Table[Pump](tag, "pumps") {
    def * = (id, poolId, installed, model).<>(Pump.tupled, Pump.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def installed = column[LocalDate]("installed")
    def model = column[String]("model")
    def poolFk = foreignKey("pool_pump_fk", poolId, TableQuery[Pools])(_.id)
    def poolId = column[Int]("pool_id")
  }

  class Timers(tag: Tag) extends Table[Timer](tag, "timers") {
    def * = (id, poolId, installed, model).<>(Timer.tupled, Timer.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def installed = column[LocalDate]("installed")
    def model = column[String]("model")
    def poolFk = foreignKey("pool_timer_fk", poolId, TableQuery[Pools])(_.id)
    def poolId = column[Int]("pool_id")
  }

  class Heaters(tag: Tag) extends Table[Heater](tag, "heaters") {
    def * = (id, poolId, installed, model).<>(Heater.tupled, Heater.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def installed = column[LocalDate]("installed")
    def model = column[String]("model")
    def poolFk = foreignKey("pool_heater_fk", poolId, TableQuery[Pools])(_.id)
    def poolId = column[Int]("pool_id")
  }

  class Lifecycles(tag: Tag) extends Table[Lifecycle](tag, "lifecycles") {
    def * = (id, poolId, created, active, pumpOn, pumpOff).<>(Lifecycle.tupled, Lifecycle.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def created = column[LocalDate]("created")
    def active = column[Boolean]("active")
    def pumpOn = column[LocalTime]("pump_on")
    def pumpOff = column[LocalTime]("pump_off")
    def poolFk = foreignKey("pool_lifecycle_fk", poolId, TableQuery[Pools])(_.id)
    def poolId = column[Int]("pool_id")
  }

  class Cleanings(tag: Tag) extends Table[Cleaning](tag, "cleanings") {
    def * = (id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter).<>(Cleaning.tupled, Cleaning.unapply)
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
    def poolFk = foreignKey("pool_cleaning_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Measurements(tag: Tag) extends Table[Measurement](tag, "measurements") {
    def * = (id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid).<>(Measurement.tupled, Measurement.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def temp = column[Double]("temp")
    def hardness = column[Double]("hardness")
    def totalChlorine = column[Double]("total_chlorine")
    def bromine = column[Double]("bromine")
    def freeChlorine = column[Double]("free_chlorine")
    def pH = column[Double]("ph")
    def alkalinity = column[Double]("alkalinity")
    def cyanuricAcid = column[Double]("cyanuric_acid")
    def poolFk = foreignKey("pool_measurement_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Additives(tag: Tag) extends Table[Additive](tag, "additives") {
    def * = (id, poolId, on, chemical, unit, amount).<>(Additive.tupled, Additive.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def on = column[LocalDate]("on")
    def chemical = column[String]("chemical")
    def unit = column[String]("unit")
    def amount = column[Double]("amount")
    def poolFk = foreignKey("pool_additive_fk", poolId, TableQuery[Pools])(_.id)
    def poolId = column[Int]("pool_id")
  }

  class Supplies(tag: Tag) extends Table[Supply](tag, "supplies") {
    def * = (id, poolId, purchased, item, unit, amount, cost).<>(Supply.tupled, Supply.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def purchased = column[LocalDate]("purchased")
    def item = column[String]("item")
    def unit = column[String]("unit")
    def amount = column[Double]("amount")
    def cost = column[Double]("cost")
    def poolFk = foreignKey("pool_supply_fk", poolId, TableQuery[Pools])(_.id)
  }

  class Repairs(tag: Tag) extends Table[Repair](tag, "repairs") {
    def * = (id, poolId, on, item, cost).<>(Repair.tupled, Repair.unapply)
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolId = column[Int]("pool_id")
    def on = column[LocalDate]("on")
    def item = column[String]("item")
    def cost = column[Double]("cost")
    def poolFk = foreignKey("pool_repair_fk", poolId, TableQuery[Pools])(_.id)
  }

  object companies extends TableQuery(new Companies(_)) {
    val compiledList = Compiled {
      sortBy(_.name)
    }
    def save(company: Company) = (this returning this.map(_.id)).insertOrUpdate(company)
    def get() = compiledList.result.headOption
  }

  object workers extends TableQuery(new Workers(_)) {
    val compiledList = Compiled { companyId: Rep[Int] => filter(_.companyId === companyId).sortBy(w => (w.last.asc, w.hired.asc)) }
    def save(worker: Worker) = (this returning this.map(_.id)).insertOrUpdate(worker)
    def list(companyId: Int) = compiledList(companyId).result
  }

  object workOrders extends TableQuery(new WorkOrders(_)) {
    val compiledListByPool = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.created.desc) }
    def save(workOrder: WorkOrder) = (this returning this.map(_.id)).insertOrUpdate(workOrder)
    def listByPool(poolId: Int) = compiledListByPool(poolId).result
  }

  object workOrderWorkers extends TableQuery(new WorkOrderWorkers(_)) {
    val compiledRemove = Compiled { workOrderWorkerId: Rep[Int] => filter(_.id === workOrderWorkerId) }
    val compiledList = Compiled { workOrderId: Rep[Int] => filter(_.workOrderId === workOrderId) }
    def save(workOrderWorker: WorkOrderWorker) = (this returning this.map(_.id)).insertOrUpdate(workOrderWorker)
    def remove(workOrderWorkerId: Int) = compiledRemove(workOrderWorkerId).delete
    def list(workOrderId: Int) = compiledList(workOrderId).result
  }

  object routeOrders extends TableQuery(new RouteOrders(_)) {
    val compiledListByRoute = Compiled { routeId: Rep[Int] => filter(_.routeId === routeId).sortBy(_.created.desc) }
    def save(routeOrder: RouteOrder) = (this returning this.map(_.id)).insertOrUpdate(routeOrder)
    def listByRoute(routeId: Int) = compiledListByRoute(routeId).result
  }

  object routeOrderWorkers extends TableQuery(new RouteOrderWorkers(_)) {
    val compiledRemove = Compiled { routeOrderWorkerId: Rep[Int] => filter(_.id === routeOrderWorkerId) }
    val compiledList = Compiled { routeOrderId: Rep[Int] => filter(_.routeOrderId === routeOrderId) }
    def save(routeOrderWorker: RouteOrderWorker) = (this returning this.map(_.id)).insertOrUpdate(routeOrderWorker)
    def remove(routeOrderWorkerId: Int) = compiledRemove(routeOrderWorkerId).delete
    def list(routeOrderId: Int) = compiledList(routeOrderId).result
  }

  object locations extends TableQuery(new Locations(_)) {
    val compiledList = Compiled { routeOrderId: Rep[Int] => filter(_.routeOrderId === routeOrderId).sortBy(_.ordinality) }
    def save(location: Location) = (this returning this.map(_.id)).insertOrUpdate(location)
    def list(routeOrderId: Int) = compiledList(routeOrderId).result
  }

  object routes extends TableQuery(new Routes(_)) {
    val compiledList = Compiled {
      sortBy(_.name)
    }
    val compiledFindById = Compiled { routeId: Rep[Int] => filter(_.id === routeId) }
    val compiledFindByName = Compiled { name: Rep[String] => filter(_.name === name) }
    def save(route: Route) = (this returning this.map(_.id)).insertOrUpdate(route)
    def find(routeId: Int) = compiledFindById(routeId).result.headOption
    def find(name: String) = compiledFindByName(name).result.headOption
    def list() = compiledList.result
  }

  object stops extends TableQuery(new Stops(_)) {
    val compiledList = Compiled { routeId: Rep[Int] => filter(_.routeId === routeId).sortBy(_.ordinality) }
    def save(stop: Stop) = stops += stop
    def list(routeId: Int) = compiledList(routeId).result
  }

  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled {
      sortBy(p => (p.zip.asc, p.city.asc))
    }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result
  }

  object owners extends TableQuery(new Owners(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(o => (o.since.desc, o.last.asc)) }
    def save(owner: Owner) = (this returning this.map(_.id)).insertOrUpdate(owner)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object surfaces extends TableQuery(new Surfaces(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(surface: Surface) = (this returning this.map(_.id)).insertOrUpdate(surface)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object pumps extends TableQuery(new Pumps(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(pump: Pump) = (this returning this.map(_.id)).insertOrUpdate(pump)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object timers extends TableQuery(new Timers(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(timer: Timer) = (this returning this.map(_.id)).insertOrUpdate(timer)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object heaters extends TableQuery(new Heaters(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.installed.desc) }
    def save(heater: Heater) = (this returning this.map(_.id)).insertOrUpdate(heater)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object lifecycles extends TableQuery(new Lifecycles(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(l => (l.active.asc, l.created.desc)) }
    def save(lifecycle: Lifecycle) = (this returning this.map(_.id)).insertOrUpdate(lifecycle)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object cleanings extends TableQuery(new Cleanings(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolId === poolid).sortBy(_.on.desc) }
    def save(cleaning: Cleaning) = (this returning this.map(_.id)).insertOrUpdate(cleaning)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object measurements extends TableQuery(new Measurements(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.on.desc) }
    def save(measurement: Measurement) = (this returning this.map(_.id)).insertOrUpdate(measurement)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object additives extends TableQuery(new Additives(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.on.desc) }
    def save(additive: Additive) = (this returning this.map(_.id)).insertOrUpdate(additive)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object supplies extends TableQuery(new Supplies(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.purchased.desc) }
    def save(supply: Supply) = (this returning this.map(_.id)).insertOrUpdate(supply)
    def list(poolId: Int) = compiledList(poolId).result
  }

  object repairs extends TableQuery(new Repairs(_)) {
    val compiledList = Compiled { poolId: Rep[Int] => filter(_.poolId === poolId).sortBy(_.on.desc) }
    def save(repair: Repair) = (this returning this.map(_.id)).insertOrUpdate(repair)
    def list(poolId: Int) = compiledList(poolId).result
  }
}