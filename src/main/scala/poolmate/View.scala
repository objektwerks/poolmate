package poolmate

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.{SplitPane, Tab, TabPane}
import scalafx.scene.layout.VBox

import poolmate.pane.*

class View(context: Context, model: Model):
  val cleaningPane = CleaningPane(context, model)
  val cleaningsTab = new Tab:
    text = context.cleanings
    closable = false
    content = cleaningPane

  val measurementPane = MeasurementPane(context, model)
  val measurementsTab = new Tab:
    text = context.measurements
    closable = false
    content = measurementPane

  val additivePane = AdditivePane(context, model)
  val additivesTab = new Tab:
    text = context.additives
    closable = false
    content = additivePane

  val lifecyclePane = LifecyclePane(context, model)
  val lifecyclesTab = new Tab:
    text = context.lifecycles
    closable = false
    content = lifecyclePane

  val supplyPane = SupplyPane(context, model)
  val suppliesTab = new Tab:
    text = context.supplies
    closable = false
    content = supplyPane

  val repairPane = RepairPane(context, model)
  val repairsTab = new Tab:
    text = context.repairs
    closable = false
    content = repairPane

  val eastTabPane = new TabPane:
    tabs = Seq(cleaningsTab, measurementsTab, additivesTab, lifecyclesTab, suppliesTab, repairsTab)
  val eastPane = new VBox:
    children = List(eastTabPane)

  val surfacePane = SurfacePane(context, model)
  val surfacesTab = new Tab:
    text = context.surfaces
    closable = false
    content = surfacePane

  val timerPane = TimerPane(context, model)
  val timersTab = new Tab:
    text = context.timers
    closable = false
    content = timerPane

  val pumpPane = PumpPane(context, model)
  val pumpsTab = new Tab:
    text = context.pumps
    closable = false
    content = pumpPane

  val heaterPane = HeaterPane(context, model)
  val heatersTab = new Tab:
    text = context.heaters
    closable = false
    content = heaterPane

  val westTabPane = new TabPane:
    padding = Insets(6)
    tabs = Seq(surfacesTab, timersTab, pumpsTab, heatersTab)

  val poolPane = PoolPane(context, model)

  val ownerPane = OwnerPane(context, model)

  val westPane = new VBox:
    children = List(poolPane, ownerPane, westTabPane)

  val menu = Menu(context)

  val splitPane = new SplitPane:
    orientation = Orientation.Horizontal; padding = Insets(6); items.addAll(westPane, eastPane)
  splitPane.setDividerPositions(0.4, 0.6)

  val contentPane = new VBox:
    prefHeight = context.height
    prefWidth = context.width
    spacing = 6
    padding = Insets(6)
    children = List(menu, splitPane)

  val scene = new Scene:
    root = contentPane
    stylesheets = List("/style.css")