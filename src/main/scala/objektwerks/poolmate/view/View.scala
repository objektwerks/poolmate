package objektwerks.poolmate.view

import com.typesafe.config.Config
import objektwerks.poolmate.model.Model
import objektwerks.poolmate.pane.MenuPane

import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{Priority, VBox}

class View(conf: Config, model: Model) {
  val westPane = new VBox { spacing = 6; padding = Insets(6); children = List() }
  val eastPane = new VBox { spacing = 6; padding = Insets(6); children = List() }

  val menuPane = new MenuPane(conf)
  val splitPane = new SplitPane { vgrow = Priority.Always; hgrow = Priority.Always; padding = Insets(6); items.addAll(westPane, eastPane) }

  val contentPane = new VBox { prefHeight = 600; prefWidth = 800; spacing = 6; padding = Insets(6); children = List(menuPane, splitPane) }
  val sceneGraph = new Scene { root = contentPane }

  model.listPools()
}