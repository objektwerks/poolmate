package poolmate

import com.typesafe.config.ConfigFactory

import org.scalatest.funsuite.AnyFunSuite

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

final class RepositoryTest extends AnyFunSuite:
  test("repository"):
    val config = DatabaseConfig.forConfig[JdbcProfile]("test", ConfigFactory.load("test.conf"))
    val repository = Repository(config, H2Profile)
    repository.createSchema()
    repository.dropSchema()
    repository.close()