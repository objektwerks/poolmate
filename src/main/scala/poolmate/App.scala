package poolmate

import com.typesafe.config.ConfigFactory

import scalafx.application.JFXApp3

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

object App extends JFXApp3:
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load("repository.conf"))
  val repository = Repository(dbConfig, H2Profile)
  val model = Model(repository)

  val config = ConfigFactory.load("app.conf")

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      scene = View(config, model).scene
      title = config.getString("title")
      icons.add(Resources.appImage)

    model.listPools()
    stage.show()
    println("*** Poolmate app started.")

  override def stopApp(): Unit =
    repository.close()
    println("*** Poolmate stopped.")