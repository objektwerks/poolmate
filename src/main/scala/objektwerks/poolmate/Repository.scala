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
  val schema = pools.schema ++ logs.schema ++ entries.schema
  val db = config.db

  def await[T](action: DBIO[T]): T = Await.result(db.run(action), awaitDuration)
  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def close() = db.close()

  def createSchema() = await(DBIO.seq(schema.create))
  def dropSchema() = await(DBIO.seq(schema.drop))

  class Pools(tag: Tag) extends Table[Pool](tag, "pools") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def owner = column[String]("name")
    def * = (id, owner) <> (Pool.tupled, Pool.unapply)
  }
  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled { sortBy(_.owner.asc) }
    def save(pool: Pool) = (this returning this.map(_.id)).insertOrUpdate(pool)
    def list() = compiledList.result
  }

  class Logs(tag: Tag) extends Table[Log](tag, "logs") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def poolid = column[Int]("pool_id")
    def created = column[LocalDate]("created")
    def * = (id, poolid, created) <> (Log.tupled, Log.unapply)
    def poolFk = foreignKey("pool_fk", poolid, TableQuery[Pools])(_.id)
  }
  object logs extends TableQuery(new Logs(_)) {
    val compiledList = Compiled { poolid: Rep[Int] => filter(_.poolid === poolid).sortBy(_.created.asc) }
    def save(log: Log) = (this returning this.map(_.id)).insertOrUpdate(log)
    def list(poolid: Int) = compiledList(poolid).result
  }

  class Entries(tag: Tag) extends Table[Entry](tag, "entries") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def logid = column[Int]("log_id")
    def created = column[LocalDate]("created")
    def * = (id, logid, created) <> (Entry.tupled, Entry.unapply)
    def logFk = foreignKey("log_fk", logid, TableQuery[Logs])(_.id)
  }
  object entries extends TableQuery(new Entries(_)) {
    val compiledList = Compiled { logid: Rep[Int] => filter(_.logid === logid).sortBy(_.created.asc) }
    def save(entry: Entry) = (this returning this.map(_.id)).insertOrUpdate(entry)
    def list(logid: Int) = compiledList(logid).result
  }
}