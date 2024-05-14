package poolmate

import com.typesafe.config.Config

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.{SplitPane, Tab, TabPane}
import scalafx.scene.layout.VBox

import poolmate.pane.*

class View(conf: Config, model: Model):
  val cleaningPane = CleaningPane(conf, model)
  val cleaningsTab = new Tab:
    text = conf.getString("cleanings")
    closable = false
    content = cleaningPane

  val measurementPane = MeasurementPane(conf, model)
  val measurementsTab = new Tab:
    text = conf.getString("measurements")
    closable = false
    content = measurementPane

  val additivePane = AdditivePane(conf, model)
  val additivesTab = new Tab:
    text = conf.getString("additives")
    closable = false
    content = additivePane

  val lifecyclePane = LifecyclePane(conf, model)
  val lifecyclesTab = new Tab:
    text = conf.getString("lifecycles")
    closable = false
    content = lifecyclePane

  val supplyPane = SupplyPane(conf, model)
  val suppliesTab = new Tab:
    text = conf.getString("supplies")
    closable = false
    content = supplyPane

  val repairPane = RepairPane(conf, model)
  val repairsTab = new Tab:
    text = conf.getString("repairs")
    closable = false
    content = repairPane

  val eastTabPane = new TabPane:
    tabs = Seq(cleaningsTab, measurementsTab, additivesTab, lifecyclesTab, suppliesTab, repairsTab)
  val eastPane = new VBox:
    children = List(eastTabPane)

  val surfacePane = SurfacePane(conf, model)
  val surfacesTab = new Tab:
    text = conf.getString("surfaces")
    closable = false
    content = surfacePane

  val timerPane = TimerPane(conf, model)
  val timersTab = new Tab:
    text = conf.getString("timers")
    closable = false
    content = timerPane

  val pumpPane = PumpPane(conf, model)
  val pumpsTab = new Tab:
    text = conf.getString("pumps")
    closable = false
    content = pumpPane

  val heaterPane = HeaterPane(conf, model)
  val heatersTab = new Tab:
    text = conf.getString("heaters")
    closable = false
    content = heaterPane

  val westTabPane = new TabPane:
    padding = Insets(6)
    tabs = Seq(surfacesTab, timersTab, pumpsTab, heatersTab)

  val poolPane = PoolPane(conf, model)

  val ownerPane = OwnerPane(conf, model)

  val westPane = new VBox:
    children = List(poolPane, ownerPane, westTabPane)

  val menuPane = MenuPane(conf)

  val splitPane = new SplitPane:
    orientation = Orientation.Horizontal; padding = Insets(6); items.addAll(westPane, eastPane)
  splitPane.setDividerPositions(0.3, 0.7)

  val contentPane = new VBox:
    prefHeight = conf.getInt("height").toDouble
    prefWidth = conf.getInt("width").toDouble
    spacing = 6
    padding = Insets(6)
    children = List(menuPane, splitPane)

  val scene = new Scene:
    root = contentPane