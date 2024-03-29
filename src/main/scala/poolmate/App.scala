package poolmate

import com.typesafe.config.ConfigFactory

import poolmate.Resources._

import scalafx.application.JFXApp3

object App extends JFXApp3 {
  val config = ConfigFactory.load("repository.conf")
  val repository = Repository(config)
  val model = Model(repository)
  val view = View(conf, model)

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      scene = view.sceneGraph
      title = conf.getString("title")
      icons.add(appImage)
    }
  }

  sys.addShutdownHook {
    repository.close()
  }
}