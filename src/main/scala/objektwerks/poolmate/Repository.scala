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

  class Pools(tag: Tag) extends Table[Student](tag, "students") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def born = column[LocalDate]("born")
    def * = (id, name, born) <> (Student.tupled, Student.unapply)
  }
  object pools extends TableQuery(new Pools(_)) {
    val compiledList = Compiled { sortBy(_.born.asc) }
    def save(student: Student) = (this returning this.map(_.id)).insertOrUpdate(student)
    def list() = compiledList.result
  }

  class Logs(tag: Tag) extends Table[Grade](tag, "grades") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def studentid = column[Int]("student_id")
    def year = column[String]("grade")
    def started = column[LocalDate]("started")
    def completed = column[LocalDate]("completed")
    def * = (id, studentid, year, started, completed) <> (Grade.tupled, Grade.unapply)
    def studentFk = foreignKey("student_fk", studentid, TableQuery[Pools])(_.id)
  }
  object logs extends TableQuery(new Logs(_)) {
    val compiledList = Compiled { studentid: Rep[Int] => filter(_.studentid === studentid).sortBy(_.started.asc) }
    def save(grade: Grade) = (this returning this.map(_.id)).insertOrUpdate(grade)
    def list(studentid: Int) = compiledList(studentid).result
  }

  class Entries(tag: Tag) extends Table[Course](tag, "courses") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def gradeid = column[Int]("grade_id")
    def name = column[String]("name")
    def started = column[LocalDate]("started")
    def completed = column[LocalDate]("completed")
    def * = (id, gradeid, name, started, completed) <> (Course.tupled, Course.unapply)
    def gradeFk = foreignKey("grade_fk", gradeid, TableQuery[Grades])(_.id)
  }
  object entries extends TableQuery(new Entries(_)) {
    val compiledList = Compiled { gradeid: Rep[Int] => filter(_.gradeid === gradeid).sortBy(_.started.asc) }
    def save(course: Course) = (this returning this.map(_.id)).insertOrUpdate(course)
    def list(gradeid: Int) = compiledList(gradeid).result
  }
}