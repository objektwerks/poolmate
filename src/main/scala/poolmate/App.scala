package poolmate

import com.typesafe.config.ConfigFactory

import scalafx.application.JFXApp3

import slick.basic.DatabaseConfig
import slick.jdbc.{H2Profile, JdbcProfile}

object App extends JFXApp3:
  val config = ConfigFactory.load("app.conf")
  val context = Context(config)

  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load("repository.conf"))
  val repository = Repository(dbConfig, H2Profile).ifAbsentInstall()
  val model = Model(repository)

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      scene = View(context, model).scene
      title = context.title
      icons += context.appImage

    model.listPools()
    stage.show()
    println("*** Poolmate app started.")

  override def stopApp(): Unit =
    repository.close()
    println("*** Poolmate app stopped.")