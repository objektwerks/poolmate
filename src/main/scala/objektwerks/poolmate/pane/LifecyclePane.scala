package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.Resources._
import objektwerks.poolmate.dialog.LifecycleDialog
import objektwerks.poolmate.entity.Lifecycle
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class LifecyclePane(conf: Config, model: Model) extends VBox {
  val lifecycleTableView = new TableView[Lifecycle]() {
    columns ++= List(
      new TableColumn[Lifecycle, String] { text = conf.getString("lifecycle-header-created"); cellValueFactory = { _.value.createdProperty } },
      new TableColumn[Lifecycle, String] { text = conf.getString("lifecycle-header-active"); cellValueFactory = { _.value.activeProperty } },
      new TableColumn[Lifecycle, String] { text = conf.getString("lifecycle-header-pump-on"); cellValueFactory = { _.value.pumpOnProperty } },
      new TableColumn[Lifecycle, String] { text = conf.getString("lifecycle-header-pump-off"); cellValueFactory = { _.value.pumpOffProperty } }
    )
    prefHeight = conf.getInt("height")
    items = model.lifecycleList
  }
  lifecycleTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val lifecycleAddButton = new Button { graphic = addImageView() }
  val lifecycleEditButton = new Button { graphic = editImageView(); disable = true }
  val lifecycleToolBar = new HBox { spacing = 6; children = List(lifecycleAddButton, lifecycleEditButton) }

  spacing = 6
  padding = Insets(6)
  children = List(lifecycleTableView, lifecycleToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listLifecycles(selectedPoolId.intValue)
    lifecycleAddButton.disable = false
  }

  lifecycleTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedLifecycle) =>
    // model.update executes a remove and add on items. the remove passes a null selectedLifecycle!
    if (selectedLifecycle != null) {
      model.selectedLifecycleId.value = selectedLifecycle.id
      lifecycleEditButton.disable = false
    }
  }

  lifecycleTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && lifecycleTableView.selectionModel().getSelectedItem != null ) update()
  }

  lifecycleAddButton.onAction = { _ => add() }

  lifecycleEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new LifecycleDialog(conf, Lifecycle(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Lifecycle(id, poolId, created, active, pumpOn, pumpOff)) =>
        val newLifecycle = model.addLifecycle(Lifecycle(id, poolId, created, active, pumpOn, pumpOff))
        lifecycleTableView.selectionModel().select(newLifecycle)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = lifecycleTableView.selectionModel().getSelectedIndex
    val lifecycle = lifecycleTableView.selectionModel().getSelectedItem.lifecycle
    new LifecycleDialog(conf, lifecycle).showAndWait() match {
      case Some(Lifecycle(id, poolId, created, active, pumpOn, pumpOff)) =>
        model.updateLifecycle(selectedIndex, Lifecycle(id, poolId, created, active, pumpOn, pumpOff))
        lifecycleTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}