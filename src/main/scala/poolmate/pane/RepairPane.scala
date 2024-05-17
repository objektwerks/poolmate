package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Model, Repair}
import poolmate.dialog.{RepairChartDialog, RepairDialog}

class RepairPane(context: Context, model: Model) extends VBox:
  val repairTableView = new TableView[Repair]:
    columns ++= List(
      new TableColumn[Repair, String]:
        text = context.repairHeaderOn
        cellValueFactory = {
          _.value.onProperty
        }
      ,
      new TableColumn[Repair, String]:
        text = context.repairHeaderItem
        cellValueFactory = {
          _.value.itemProperty
        }
      ,
      new TableColumn[Repair, String]:
        text = context.repairHeaderCost
        cellValueFactory = {
          _.value.costProperty
        }
    )
    prefHeight = context.height
    items = model.repairList
  repairTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val repairAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val repairEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val repairChartButton = new Button:
    graphic = context.lineChartImageView
    disable = true

  val repairToolBar = new HBox:
    spacing = 6
    children = List(repairAddButton, repairEditButton, repairChartButton)

  spacing = 6
  padding = Insets(6)
  children = List(repairTableView, repairToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listRepairs(selectedPoolId.intValue)
    repairAddButton.disable = false
    repairChartButton.disable = if (model.repairList.nonEmpty) then false else true
  }

  repairTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedRepair) =>
    // model.update executes a remove and add on items. the remove passes a null selectedRepair!
    if (selectedRepair != null) then
      model.selectedRepairId.value = selectedRepair.id
      repairEditButton.disable = false
      repairChartButton.disable = false
  }

  repairTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        repairTableView.selectionModel().getSelectedItem != null) then update()
  }

  repairAddButton.onAction = { _ => add() }

  repairEditButton.onAction = { _ => update() }

  repairChartButton.onAction = { _ => RepairChartDialog(context, model).showAndWait() }

  def add(): Unit =
    RepairDialog(context, Repair(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Repair(id, poolId, on, item, cost)) =>
        val newRepair = model.addRepair(
          Repair(id, poolId, on, item, cost)
        )
        repairTableView.selectionModel().select(newRepair)
      case _ =>

  def update(): Unit =
    val selectedIndex = repairTableView.selectionModel().getSelectedIndex
    val repair = repairTableView.selectionModel().getSelectedItem.repair
    RepairDialog(context, repair).showAndWait() match
      case Some(Repair(id, poolId, on, item, cost)) =>
        model.updateRepair(
          selectedIndex,
          Repair(id, poolId, on, item, cost)
        )
        repairTableView.selectionModel().select(selectedIndex)
      case _ =>