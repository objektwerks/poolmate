package objektwerks.poolmate

import com.typesafe.config.ConfigFactory
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model
import objektwerks.poolmate.repository.Repository
import objektwerks.poolmate.view.View

import scalafx.application.JFXApp

object App extends JFXApp {
  val conf = ConfigFactory.load("app.conf")
  val repository = Repository.newInstance("repository.conf")
  val model = new Model(repository)
  val view = new View(conf, model)
  stage = new JFXApp.PrimaryStage {
    scene = view.sceneGraph
    title = conf.getString("title")
    icons.add(Images.appImage())
  }

  sys.addShutdownHook { repository.close() }
}