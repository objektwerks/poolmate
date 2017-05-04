package objektwerks.poolmate.view

import com.typesafe.config.Config
import objektwerks.poolmate.model.Model
import objektwerks.poolmate.pane._

import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.{SplitPane, Tab, TabPane}
import scalafx.scene.layout.{Priority, VBox}

class View(conf: Config, model: Model) {
  val supplyPane = new SupplyPane(conf, model)
  val suppliesTab = new Tab { text = conf.getString("supplies-tab"); closable = false; content = supplyPane }

  val lifecyclePane = new LifecyclePane(conf, model)
  val lifecyclesTab = new Tab { text = conf.getString("lifecycles-tab"); closable = false; content = lifecyclePane }

  val cleaningPane = new CleaningPane(conf, model)
  val cleaningsTab = new Tab { text = conf.getString("cleanings-tab"); closable = false; content = cleaningPane }

  val measurementPane = new MeasurementPane(conf, model)
  val measurementsTab = new Tab { text = conf.getString("measurements-tab"); closable = false; content = measurementPane }

  val additivePane = new AdditivePane(conf, model)
  val additivesTab = new Tab { text = conf.getString("additives-tab"); closable = false; content = additivePane }

  val eastTabPane = new TabPane { padding = Insets(6); tabs = ObservableBuffer(suppliesTab, lifecyclesTab, cleaningsTab, measurementsTab, additivesTab) }

  val surfacePane = new SurfacePane(conf, model)
  val surfacesTab = new Tab { text = conf.getString("surfaces-tab"); closable = false; content = surfacePane }

  val timerPane = new TimerPane(conf, model)
  val timersTab = new Tab { text = conf.getString("timers-tab"); closable = false; content = timerPane }

  val pumpPane = new PumpPane(conf, model)
  val pumpsTab = new Tab { text = conf.getString("pumps-tab"); closable = false; content = pumpPane }

  val heaterPane = new HeaterPane(conf, model)
  val heatersTab = new Tab { text = conf.getString("heaters-tab"); closable = false; content = heaterPane }

  val repairPane = new RepairPane(conf, model)
  val repairsTab = new Tab { text = conf.getString("repairs-tab"); closable = false; content = repairPane }

  val westTabPane = new TabPane { padding = Insets(6); tabs = ObservableBuffer(surfacesTab, timersTab, pumpsTab, heatersTab, repairsTab) }

  val poolPane = new PoolPane(conf, model)
  val ownerPane = new OwnerPane(conf, model)
  val westPane = new VBox { children = List(poolPane, ownerPane, westTabPane) }

  val menuPane = new MenuPane(conf)
  val splitPane = new SplitPane { orientation = Orientation.Horizontal; vgrow = Priority.Always; hgrow = Priority.Always; padding = Insets(6); items.addAll(westPane, eastTabPane) }
  splitPane.setDividerPositions(0.4, 0.6)
  splitPane.autosize()

  val contentPane = new VBox { prefHeight = conf.getInt("height"); prefWidth = conf.getInt("width"); spacing = 6; padding = Insets(6); children = List(menuPane, splitPane) }
  val sceneGraph = new Scene { root = contentPane }

  model.listPools()
}