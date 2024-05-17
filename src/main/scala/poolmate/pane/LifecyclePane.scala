package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Lifecycle, Model}
import poolmate.dialog.LifecycleDialog

class LifecyclePane(context: Context, model: Model) extends VBox:
  val lifecycleTableView = new TableView[Lifecycle]:
    columns ++= List(
      new TableColumn[Lifecycle, String]:
        text = context.lifecycleHeaderCreated
        cellValueFactory = {
          _.value.createdProperty
        }
      ,
      new TableColumn[Lifecycle, String]:
        text = context.lifecycleHeaderActive
        cellValueFactory = {
          _.value.activeProperty
        }
      ,
      new TableColumn[Lifecycle, String]:
        text = context.lifecycleHeaderPumpOn
        cellValueFactory = {
          _.value.pumpOnProperty
        }
      ,
      new TableColumn[Lifecycle, String]:
        text = context.lifecycleHeaderPumpOff
        cellValueFactory = {
          _.value.pumpOffProperty
        }
    )
    prefHeight = context.height.toDouble
    items = model.lifecycleList
  lifecycleTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val lifecycleAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val lifecycleEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val lifecycleToolBar = new HBox:
    spacing = 6
    children = List(lifecycleAddButton, lifecycleEditButton)

  spacing = 6
  padding = Insets(6)
  children = List(lifecycleTableView, lifecycleToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listLifecycles(selectedPoolId.intValue)
    lifecycleAddButton.disable = false
  }

  lifecycleTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedLifecycle) =>
    // model.update executes a remove and add on items. the remove passes a null selectedLifecycle!
    if (selectedLifecycle != null) then
      model.selectedLifecycleId.value = selectedLifecycle.id
      lifecycleEditButton.disable = false
  }

  lifecycleTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        lifecycleTableView.selectionModel().getSelectedItem != null) then update()
  }

  lifecycleAddButton.onAction = { _ => add() }

  lifecycleEditButton.onAction = { _ => update() }

  def add(): Unit =
    LifecycleDialog(context, Lifecycle(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Lifecycle(id, poolId, created, active, pumpOn, pumpOff)) =>
        val newLifecycle = model.addLifecycle(
          Lifecycle(id, poolId, created, active, pumpOn, pumpOff)
        )
        lifecycleTableView.selectionModel().select(newLifecycle)
      case _ =>

  def update(): Unit =
    val selectedIndex = lifecycleTableView.selectionModel().getSelectedIndex
    val lifecycle = lifecycleTableView.selectionModel().getSelectedItem.lifecycle
    LifecycleDialog(context, lifecycle).showAndWait() match
      case Some(Lifecycle(id, poolId, created, active, pumpOn, pumpOff)) =>
        model.updateLifecycle(
          selectedIndex,
          Lifecycle(id, poolId, created, active, pumpOn, pumpOff)
        )
        lifecycleTableView.selectionModel().select(selectedIndex)
      case _ =>