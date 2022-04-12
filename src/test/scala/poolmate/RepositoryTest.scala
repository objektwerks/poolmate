package poolmate

import org.scalatest.funsuite.AnyFunSuite

class RepositoryTest extends AnyFunSuite {
  test("repository") {
    val repository = Repository("test.conf")
    repository.schema.createStatements foreach println
    repository.close()
  }
}