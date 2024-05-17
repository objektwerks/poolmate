package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Model, Supply}
import poolmate.dialog.{SupplyChartDialog, SupplyDialog}

class SupplyPane(context: Context, model: Model) extends VBox:
  val supplyTableView = new TableView[Supply]:
    columns ++= List(
      new TableColumn[Supply, String]:
        text = context.supplyHeaderPurchased
        cellValueFactory = {
          _.value.purchasedProperty
        }
      ,
      new TableColumn[Supply, String]:
        text = context.supplyHeaderItem
        cellValueFactory = {
          _.value.itemProperty
        }
      ,
      new TableColumn[Supply, String]:
        text = context.supplyHeaderUnit
        cellValueFactory = {
          _.value.unitProperty
        }
      ,
      new TableColumn[Supply, String]:
        text = context.supplyHeaderAmount
        cellValueFactory = {
          _.value.amountProperty
        }
      ,
      new TableColumn[Supply, String]:
        text = context.supplyHeaderCost
        cellValueFactory = {
          _.value.costProperty
        }
    )
    prefHeight = context.height
    items = model.supplyList

  supplyTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val supplyAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val supplyEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val supplyChartButton = new Button:
    graphic = context.barChartImageView
    disable = true

  val supplyToolBar = new HBox:
    spacing = 6
    children = List(supplyAddButton, supplyEditButton, supplyChartButton)

  spacing = 6
  padding = Insets(6)
  children = List(supplyTableView, supplyToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listSupplies(selectedPoolId.intValue)
    supplyAddButton.disable = false
    supplyChartButton.disable = if (model.supplyList.nonEmpty) then false else true
  }

  supplyTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedSupply) =>
    // model.update executes a remove and add on items. the remove passes a null selectedSupply!
    if (selectedSupply != null) then
      model.selectedSupplyId.value = selectedSupply.id
      supplyEditButton.disable = false
      supplyChartButton.disable = false
  }

  supplyTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        supplyTableView.selectionModel().getSelectedItem != null) then update()
  }

  supplyAddButton.onAction = { _ => add() }

  supplyEditButton.onAction = { _ => update() }

  supplyChartButton.onAction = { _ => SupplyChartDialog(context, model).showAndWait() }

  def add(): Unit =
    SupplyDialog(context, Supply(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Supply(id, poolId, purchased, item, unit, amount, cost)) =>
        val newSupply = model.addSupply(
          Supply(id, poolId, purchased, item, unit, amount, cost)
        )
        supplyTableView.selectionModel().select(newSupply)
      case _ =>

  def update(): Unit = 
    val selectedIndex = supplyTableView.selectionModel().getSelectedIndex
    val supply = supplyTableView.selectionModel().getSelectedItem.supply
    SupplyDialog(context, supply).showAndWait() match
      case Some(Supply(id, poolId, purchased, item, unit, amount, cost)) =>
        model.updateSupply(
          selectedIndex,
          Supply(id, poolId, purchased, item, unit, amount, cost)
        )
        supplyTableView.selectionModel().select(selectedIndex)
      case _ =>