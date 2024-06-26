package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Heater, Model}
import poolmate.dialog.HeaterDialog

class HeaterPane(context: Context, model: Model) extends VBox:
  val heaterTableView = new TableView[Heater]:
    columns ++= List(
      new TableColumn[Heater, String]:
        text = context.heaterHeaderInstalled
        cellValueFactory = {
          _.value.installedProperty
        }
      ,
      new TableColumn[Heater, String]:
        text = context.heaterHeaderModel
        cellValueFactory = {
          _.value.modelProperty
        }
    )
    items = model.heaterList
  heaterTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val heaterAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val heaterEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val heaterToolBar = new HBox:
    spacing = 6; children = List(heaterAddButton, heaterEditButton)

  spacing = 6
  padding = Insets(6)
  children = List(heaterTableView, heaterToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listHeaters(selectedPoolId.intValue)
    heaterAddButton.disable = false
  }

  heaterTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedHeater) =>
    // model.update executes a remove and add on items. the remove passes a null selectedHeater!
    if (selectedHeater != null) then
      model.selectedHeaterId.value = selectedHeater.id
      heaterEditButton.disable = false
  }

  heaterTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        heaterTableView.selectionModel().getSelectedItem != null) then update()
  }

  heaterAddButton.onAction = { _ => add() }

  heaterEditButton.onAction = { _ => update() }

  def add(): Unit =
    HeaterDialog(context, Heater(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Heater(id, poolId, installed, _model)) =>
        val newHeater = model.addHeater(
          Heater(id, poolId, installed, _model)
        )
        heaterTableView.selectionModel().select(newHeater)
      case _ =>

  def update(): Unit =
    val selectedIndex = heaterTableView.selectionModel().getSelectedIndex
    val heater = heaterTableView.selectionModel().getSelectedItem.heater
    HeaterDialog(context, heater).showAndWait() match
      case Some(Heater(id, poolId, installed, _model)) =>
        model.updateHeater(
          selectedIndex,
          Heater(id, poolId, installed, _model)
        )
        heaterTableView.selectionModel().select(selectedIndex)
      case _ =>