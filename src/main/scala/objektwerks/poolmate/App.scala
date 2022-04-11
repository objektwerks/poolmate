package objektwerks.poolmate

import objektwerks.poolmate.Resources._
import objektwerks.poolmate.model.Model
import objektwerks.poolmate.repository.Repository
import objektwerks.poolmate.view.View

import scalafx.application.JFXApp3

object App extends JFXApp3 {
  val repository = Repository("repository.conf")
  val model = Model(repository)
  val view = View(conf, model)

  stage = new JFXApp3.PrimaryStage {
    scene = view.sceneGraph
    title = conf.getString("title")
    icons.add(appImage)
  }

  override def start(): Unit = ()

  sys.addShutdownHook {
    repository.close()
  }
}