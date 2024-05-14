package poolmate.pane

import com.typesafe.config.Config

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Model, Pump}
import poolmate.Context.*
import poolmate.dialog.PumpDialog

class PumpPane(conf: Config, model: Model) extends VBox:
  val pumpTableView = new TableView[Pump]:
    columns ++= List(
      new TableColumn[Pump, String]:
        text = conf.getString("pump-header-installed")
        cellValueFactory = {
          _.value.installedProperty
        }
      ,
      new TableColumn[Pump, String]:
        text = conf.getString("pump-header-model")
        cellValueFactory = {
          _.value.modelProperty
        }
    )
    items = model.pumpList
  pumpTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val pumpAddButton = new Button:
    graphic = addImageView
    disable = true

  val pumpEditButton = new Button:
    graphic = editImageView
    disable = true

  val pumpToolBar = new HBox:
    spacing = 6
    children = List(pumpAddButton, pumpEditButton)

  spacing = 6
  padding = Insets(6)
  children = List(pumpTableView, pumpToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listPumps(selectedPoolId.intValue)
    pumpAddButton.disable = false
  }

  pumpTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedPump) =>
    // model.update executes a remove and add on items. the remove passes a null selectedPump!
    if (selectedPump != null) then
      model.selectedPumpId.value = selectedPump.id
      pumpEditButton.disable = false
  }

  pumpTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        pumpTableView.selectionModel().getSelectedItem != null) then update()
  }

  pumpAddButton.onAction = { _ => add() }

  pumpEditButton.onAction = { _ => update() }

  def add(): Unit = 
    PumpDialog(conf, Pump(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Pump(id, poolId, installed, _model)) =>
        val newPump = model.addPump(
          Pump(id, poolId, installed, _model)
        )
        pumpTableView.selectionModel().select(newPump)
      case _ =>

  def update(): Unit =
    val selectedIndex = pumpTableView.selectionModel().getSelectedIndex
    val pump = pumpTableView.selectionModel().getSelectedItem.pump
    PumpDialog(conf, pump).showAndWait() match
      case Some(Pump(id, poolId, installed, _model)) =>
        model.updatePump(
          selectedIndex,
          Pump(id, poolId, installed, _model)
        )
        pumpTableView.selectionModel().select(selectedIndex)
      case _ =>