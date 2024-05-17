package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Model, Pool}
import poolmate.dialog.PoolDialog

class PoolPane(context: Context, model: Model) extends VBox:
  val poolLabel = new Label:
    text = context.pools

  val poolTableView = new TableView[Pool]:
    columns ++= List(
      new TableColumn[Pool, String]:
        text = context.poolHeaderBuilt
        cellValueFactory = {
          _.value.builtProperty
        }
      ,
      new TableColumn[Pool, String]:
        text = context.poolHeaderGallons
        cellValueFactory = {
          _.value.gallonsProperty
        }
      ,
      new TableColumn[Pool, String]:
        text = context.poolHeaderStreet
        cellValueFactory = {
          _.value.streetProperty
        }
      ,
      new TableColumn[Pool, String]:
        text = context.poolHeaderCity
        cellValueFactory = {
          _.value.cityProperty
        }
      ,
      new TableColumn[Pool, String]:
        text = context.poolHeaderState
        cellValueFactory = {
          _.value.stateProperty
        }
      ,
      new TableColumn[Pool, String]:
        text = context.poolHeaderZip
        cellValueFactory = {
          _.value.zipProperty
        }
    )
    items = model.poolList
  poolTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val poolAddButton = new Button:
    graphic = context.addImageView

  val poolEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val poolToolBar = new HBox:
    spacing = 6
    children = List(poolAddButton, poolEditButton)

  spacing = 6
  padding = Insets(6)
  children = List(poolLabel, poolTableView, poolToolBar)

  poolTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedPool) =>
    // model.update executes a remove and add on items. the remove passes a null selectedPool!
    if (selectedPool != null) then
      model.selectedPoolId.value = selectedPool.id
      poolEditButton.disable = false
  }

  poolTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        poolTableView.selectionModel().getSelectedItem != null) then update()
  }

  poolAddButton.onAction = { _ => add() }

  poolEditButton.onAction = { _ => update() }

  def add(): Unit =
    new PoolDialog(context, Pool()).showAndWait() match 
      case Some(Pool(id, built, gallons, street, city, state, zip)) =>
        val newPool = model.addPool(
          Pool(id, built, gallons, street, city, state, zip)
        )
        poolTableView.selectionModel().select(newPool)
      case _ =>

  def update(): Unit = 
    val selectedIndex = poolTableView.selectionModel().getSelectedIndex
    val pool = poolTableView.selectionModel().getSelectedItem.pool
    PoolDialog(context, pool).showAndWait() match
      case Some(Pool(id, built, gallons, street, city, state, zip)) =>
        model.updatePool(
          selectedIndex,
          Pool(id, built, gallons, street, city, state, zip)
        )
        poolTableView.selectionModel().select(selectedIndex)
      case _ =>