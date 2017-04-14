package objektwerks.poolmate.view

import com.typesafe.config.Config
import objektwerks.poolmate.model.Model

import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.VBox

class View(conf: Config, model: Model) {
  val contentPane = new VBox { prefHeight = 600; prefWidth = 800; spacing = 6; padding = Insets(6); children = List() }
  val sceneGraph = new Scene { root = contentPane }

  model.listOwners()
}