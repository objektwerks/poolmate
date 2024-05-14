package poolmate

import com.typesafe.config.ConfigFactory

import scalafx.application.JFXApp3

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

object App extends JFXApp3:
  val config = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load("repository.conf"))
  val repository = Repository(config, H2Profile)
  val model = Model(repository)

  val resources = ConfigFactory.load("resources.conf")

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      scene = View(resources, model).scene
      title = resources.getString("title")
      icons.add(Resources.appImage)

    model.listPools()
    stage.show()
    println("*** Poolmate app started.")

  override def stopApp(): Unit =
    repository.close()
    println("*** Poolmate stopped.")