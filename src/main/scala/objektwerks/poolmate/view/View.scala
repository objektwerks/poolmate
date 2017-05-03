package objektwerks.poolmate.view

import com.typesafe.config.Config
import objektwerks.poolmate.model.Model
import objektwerks.poolmate.pane._

import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{Priority, VBox}

class View(conf: Config, model: Model) {
  val poolPane = new PoolPane(conf, model)
  val ownerPane = new OwnerPane(conf, model)
  val surfacePane = new SurfacePane(conf, model)
  val timerPane = new TimerPane(conf, model)
  val pumpPane = new PumpPane(conf, model)
  val heaterPane = new HeaterPane(conf, model)
  val westPane = new VBox { spacing = 6; padding = Insets(6); children = List(poolPane, ownerPane, surfacePane, pumpPane, timerPane, heaterPane) }

  val lifecyclePane = new LifecyclePane(conf, model)
  val cleaningPane = new CleaningPane(conf, model)
  val measurementPane = new MeasurementPane(conf, model)
  val additivePane = new AdditivePane(conf, model)
  val supplyPane = new SupplyPane(conf, model)
  val repairPane = new RepairPane(conf, model)
  val eastPane = new VBox { spacing = 6; padding = Insets(6); children = List(lifecyclePane, cleaningPane, measurementPane, additivePane, supplyPane, repairPane) }

  val menuPane = new MenuPane(conf)
  val splitPane = new SplitPane { vgrow = Priority.Always; hgrow = Priority.Always; padding = Insets(6); items.addAll(westPane, eastPane) }

  val contentPane = new VBox { prefHeight = 600; prefWidth = 800; spacing = 6; padding = Insets(6); children = List(menuPane, splitPane) }
  val sceneGraph = new Scene { root = contentPane }

  model.listPools()
}