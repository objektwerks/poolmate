package poolmate

import com.typesafe.config.ConfigFactory

import org.scalatest.funsuite.AnyFunSuite

class RepositoryTest extends AnyFunSuite {
  test("repository") {
    val config = ConfigFactory.load("test.conf")
    val repository = Repository(config)
    repository.schema.createStatements foreach println
    repository.close()
  }
}