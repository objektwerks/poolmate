package objektwerks.poolmate

import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}

class RepositoryTest extends FunSuite with BeforeAndAfterAll with Matchers {
  val repository = Repository.newInstance("test.conf")
  import repository._

  override protected def beforeAll(): Unit = {
    schema.createStatements foreach println
  }

  override protected def afterAll(): Unit = {
    close()
  }

  test("repository") {
  }
}