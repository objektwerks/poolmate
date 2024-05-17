package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Additive, Context, Model}
import poolmate.dialog.{AdditiveChartDialog, AdditiveDialog}

class AdditivePane(context: Context, model: Model) extends VBox:
  val additiveTableView = new TableView[Additive]:
    columns ++= List(
      new TableColumn[Additive, String]:
        text = context.additiveHeaderOn
        cellValueFactory = {
          _.value.onProperty
        }
      ,
      new TableColumn[Additive, String]:
        text = context.additiveHeaderChemical
        cellValueFactory = {
          _.value.chemicalProperty
        }
      ,
      new TableColumn[Additive, String]:
        text = context.additiveHeaderUnit
        cellValueFactory = {
          _.value.unitProperty
        }
      ,
      new TableColumn[Additive, String]:
        text = context.additiveHeaderAmount
        cellValueFactory = {
          _.value.amountProperty
        }
    )
    prefHeight = context.height.toDouble
    items = model.additiveList
  additiveTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val additiveAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val additiveEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val additiveChartButton = new Button:
    graphic = context.barChartImageView
    disable = true

  val additiveToolBar = new HBox:
    spacing = 6
    children = List(additiveAddButton, additiveEditButton, additiveChartButton)

  spacing = 6
  padding = Insets(6)
  children = List(additiveTableView, additiveToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listAdditives(selectedPoolId.intValue)
    additiveAddButton.disable = false
    additiveChartButton.disable = if (model.additiveList.nonEmpty) then false else true
  }

  additiveTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedAdditive) =>
    // model.update executes a remove and add on items. the remove passes a null selectedAdditive!
    if (selectedAdditive != null) then
      model.selectedAdditiveId.value = selectedAdditive.id
      additiveEditButton.disable = false
      additiveChartButton.disable = false
  }

  additiveTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        additiveTableView.selectionModel().getSelectedItem != null) then update()
  }

  additiveAddButton.onAction = { _ => add() }

  additiveEditButton.onAction = { _ => update() }

  additiveChartButton.onAction = { _ => AdditiveChartDialog(context, model).showAndWait() }

  def add(): Unit =
    AdditiveDialog(context, Additive(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Additive(id, poolId, on, chemical, unit, amount)) =>
        val newAdditive = model.addAdditive(
          Additive(id, poolId, on, chemical, unit, amount)
        )
        additiveTableView.selectionModel().select(newAdditive)
      case _ =>

  def update(): Unit =
    val selectedIndex = additiveTableView.selectionModel().getSelectedIndex
    val additive = additiveTableView.selectionModel().getSelectedItem.additive
    AdditiveDialog(context, additive).showAndWait() match
      case Some(Additive(id, poolId, on, chemical, unit, amount)) =>
        model.updateAdditive(
          selectedIndex,
          Additive(id, poolId, on, chemical, unit, amount)
        )
        additiveTableView.selectionModel().select(selectedIndex)
      case _ =>