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
  val poolPane = new PoolPane(conf, model)
  val poolsContent = new VBox { spacing = 6; padding = Insets(6); children = List(poolPane) }
  val poolsTab = new Tab { text = conf.getString("pools-tab"); content = poolsContent }

  val ownerPane = new OwnerPane(conf, model)
  val ownersContent = new VBox { spacing = 6; padding = Insets(6); children = List(ownerPane) }
  val ownersTab = new Tab { text = conf.getString("owners-tab"); content = ownersContent }

  val supplyPane = new SupplyPane(conf, model)
  val suppliesContent = new VBox { spacing = 6; padding = Insets(6); children = List(supplyPane) }
  val suppliesTab = new Tab { text = conf.getString("supplies-tab"); content = suppliesContent }

  val lifecyclePane = new LifecyclePane(conf, model)
  val lifecyclesContent = new VBox { spacing = 6; padding = Insets(6); children = List(lifecyclePane) }
  val lifecyclesTab = new Tab { text = conf.getString("lifecycles-tab"); content = lifecyclesContent }

  val cleaningPane = new CleaningPane(conf, model)
  val cleaningsContent = new VBox { spacing = 6; padding = Insets(6); children = List(cleaningPane) }
  val cleaningsTab = new Tab { text = conf.getString("cleanings-tab"); content = cleaningsContent }

  val measurementPane = new MeasurementPane(conf, model)
  val measurementsContent = new VBox { spacing = 6; padding = Insets(6); children = List(measurementPane) }
  val measurementsTab = new Tab { text = conf.getString("measurements-tab"); content = measurementsContent }

  val additivePane = new AdditivePane(conf, model)
  val additivesContent = new VBox { spacing = 6; padding = Insets(6); children = List(additivePane) }
  val additivesTab = new Tab { text = conf.getString("additives-tab"); content = additivesContent }

  val northPane = new TabPane { padding = Insets(6); tabs = ObservableBuffer(poolsTab, ownersTab, suppliesTab, lifecyclesTab, cleaningsTab, measurementsTab, additivesTab) }

  val surfacePane = new SurfacePane(conf, model)
  val surfacesContent = new VBox { spacing = 6; padding = Insets(6); children = List(surfacePane) }
  val surfacesTab = new Tab { text = conf.getString("surfaces-tab"); content = surfacesContent }

  val timerPane = new TimerPane(conf, model)
  val timersContent = new VBox { spacing = 6; padding = Insets(6); children = List(timerPane) }
  val timersTab = new Tab { text = conf.getString("timers-tab"); content = timersContent }

  val pumpPane = new PumpPane(conf, model)
  val pumpsContent = new VBox { spacing = 6; padding = Insets(6); children = List(pumpPane) }
  val pumpsTab = new Tab { text = conf.getString("pumps-tab"); content = pumpsContent }

  val heaterPane = new HeaterPane(conf, model)
  val heatersContent = new VBox { spacing = 6; padding = Insets(6); children = List(heaterPane) }
  val heatersTab = new Tab { text = conf.getString("heaters-tab"); content = heatersContent }

  val repairPane = new RepairPane(conf, model)
  val repairsContent = new VBox { spacing = 6; padding = Insets(6); children = List(repairPane) }
  val repairsTab = new Tab { text = conf.getString("repairs-tab"); content = repairsContent }

  val southPane = new TabPane { padding = Insets(6); tabs = ObservableBuffer(surfacesTab, timersTab, pumpsTab, heatersTab, repairsTab) }

  val menuPane = new MenuPane(conf)
  val splitPane = new SplitPane { orientation = Orientation.Vertical ; vgrow = Priority.Always; hgrow = Priority.Always; padding = Insets(6); items.addAll(northPane, southPane) }
  splitPane.setDividerPositions(0.7, 0.3)
  splitPane.autosize()

  val contentPane = new VBox { prefHeight = conf.getInt("height"); prefWidth = conf.getInt("width"); spacing = 6; padding = Insets(6); children = List(menuPane, splitPane) }
  val sceneGraph = new Scene { root = contentPane }

  model.listPools()
}