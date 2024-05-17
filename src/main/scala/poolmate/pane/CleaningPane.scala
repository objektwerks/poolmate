package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Cleaning, Context, Model}
import poolmate.Context.*
import poolmate.dialog.CleaningDialog

class CleaningPane(context: Context, model: Model) extends VBox:
  val cleaningTableView = new TableView[Cleaning]:
    columns ++= List(
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderOn
        cellValueFactory = {
          _.value.onProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderDeck
        cellValueFactory = {
          _.value.deckProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderBrush
        cellValueFactory = {
          _.value.brushProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderNet
        cellValueFactory = {
          _.value.netProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderVacuum
        cellValueFactory = {
          _.value.vacuumProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderSkimmerBasket
        cellValueFactory = {
          _.value.skimmerBasketProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderPumpBasket
        cellValueFactory = {
          _.value.pumpBasketProperty
        }
      ,
      new TableColumn[Cleaning, String]:
        text = context.cleaningHeaderPumpFilter
        cellValueFactory = {
          _.value.pumpFilterProperty
        }
    )
    prefHeight = context.height.toDouble
    items = model.cleaningList
  cleaningTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val cleaningAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val cleaningEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val cleaningToolBar = new HBox:
    spacing = 6
    children = List(cleaningAddButton, cleaningEditButton)

  spacing = 6
  padding = Insets(6)
  children = List(cleaningTableView, cleaningToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listCleanings(selectedPoolId.intValue)
    cleaningAddButton.disable = false
  }

  cleaningTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedCleaning) =>
    // model.update executes a remove and add on items. the remove passes a null selectedCleaning!
    if (selectedCleaning != null) then
      model.selectedCleaningId.value = selectedCleaning.id
      cleaningEditButton.disable = false
  }

  cleaningTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        cleaningTableView.selectionModel().getSelectedItem != null) then update()
  }

  cleaningAddButton.onAction = { _ => add() }

  cleaningEditButton.onAction = { _ => update() }

  def add(): Unit =
    CleaningDialog(context, Cleaning(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter)) =>
        val newCleaning = model.addCleaning(
          Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter)
        )
        cleaningTableView.selectionModel().select(newCleaning)
      case _ =>

  def update(): Unit =
    val selectedIndex = cleaningTableView.selectionModel().getSelectedIndex
    val cleaning = cleaningTableView.selectionModel().getSelectedItem.cleaning
    CleaningDialog(context, cleaning).showAndWait() match
      case Some(Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter)) =>
        model.updateCleaning(
          selectedIndex,
          Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter)
        )
        cleaningTableView.selectionModel().select(selectedIndex)
      case _ =>