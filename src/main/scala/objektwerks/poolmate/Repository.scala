package objektwerks.poolmate

import java.sql.Date
import java.time.LocalDate

import com.typesafe.config.ConfigFactory
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

  implicit val dateMapper = MappedColumnType.base[LocalDate, Date](ld => Date.valueOf(ld),d => d.toLocalDate)
  val schema = pools.schema ++ measurements.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def close() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  class Pools(tag: Tag) extends Table[Pool](tag, "pools") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def gallons = column[Double]("gallons")
    def owner = column[String]("owner")
    def * = (id, gallons, owner) <> (Pool.tupled, Pool.unapply)
  }
  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled { sortBy(_.owner.asc) }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result
  }

  class Measurements(tag: Tag) extends Table[Measurement](tag, "measurements") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolid = column[Int]("pool_id")
    def created = column[LocalDate]("created")
    def * = (id, poolid, created) <> (Measurement.tupled, Measurement.unapply)
    def poolFk = foreignKey("pool_fk", poolid, TableQuery[Pools])(_.id)
  }
  object measurements extends TableQuery(new Measurements(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolid === poolid).sortBy(_.created.asc) }
    def save(measurement: Measurement) = (this returning this.map(_.id)).insertOrUpdate(measurement)
    def list(poolid: Int) = compiledList(poolid).result
  }
}