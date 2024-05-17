package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Model, Owner}
import poolmate.dialog.OwnerDialog

class OwnerPane(context: Context, model: Model) extends VBox:
  val ownerLabel = new Label:
    text = context.owners

  val ownerTableView = new TableView[Owner]:
    columns ++= List(
      new TableColumn[Owner, String]:
        text = context.ownerHeaderSince
        cellValueFactory = {
          _.value.sinceProperty
        }
      ,
      new TableColumn[Owner, String]:
        text = context.ownerHeaderFirst
        cellValueFactory = {
          _.value.firstProperty
        }
      ,
      new TableColumn[Owner, String]:
        text = context.ownerHeaderLast
        cellValueFactory = {
          _.value.lastProperty
        }
      ,
      new TableColumn[Owner, String]:
        text = context.ownerHeaderEmail
        cellValueFactory = {
          _.value.emailProperty
        }
    )
    items = model.ownerList
  ownerTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val ownerAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val ownerEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val ownerToolBar = new HBox:
    spacing = 6
    children = List(ownerAddButton, ownerEditButton)

  spacing = 6
  padding = Insets(6)
  children = List(ownerLabel, ownerTableView, ownerToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listOwners(selectedPoolId.intValue)
    ownerAddButton.disable = false
  }

  ownerTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedOwner) =>
    // model.update executes a remove and add on items. the remove passes a null selectedOwner!
    if (selectedOwner != null) then
      model.selectedOwnerId.value = selectedOwner.id
      ownerEditButton.disable = false
  }

  ownerTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        ownerTableView.selectionModel().getSelectedItem != null) then update()
  }

  ownerAddButton.onAction = { _ => add() }

  ownerEditButton.onAction = { _ => update() }

  def add(): Unit =
    OwnerDialog(context, Owner(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Owner(id, poolId, since, first, last, email)) =>
        val newOwner = model.addOwner(
          Owner(id, poolId, since, first, last, email)
        )
        ownerTableView.selectionModel().select(newOwner)
      case _ =>

  def update(): Unit =
    val selectedIndex = ownerTableView.selectionModel().getSelectedIndex
    val owner = ownerTableView.selectionModel().getSelectedItem.owner
    OwnerDialog(context, owner).showAndWait() match
      case Some(Owner(id, poolId, since, first, last, email)) =>
        model.updateOwner(
          selectedIndex,
          Owner(id, poolId, since, first, last, email)
        )
        ownerTableView.selectionModel().select(selectedIndex)
      case _ =>