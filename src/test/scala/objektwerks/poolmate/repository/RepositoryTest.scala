package objektwerks.poolmate.repository

import org.scalatest.FunSuite

class RepositoryTest extends FunSuite {
  test("repository") {
    val repository = Repository.newInstance("test.conf")
    repository.schema.createStatements foreach println
    repository.close()
  }
}