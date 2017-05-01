package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.OwnerDialog
import objektwerks.poolmate.entity.Owner
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class OwnerPane(conf: Config, model: Model) extends VBox  {
  val ownerLabel = new Label { text = conf.getString("owners") }
  val ownerTableView = new TableView[Owner]() {
    columns ++= List(
      new TableColumn[Owner, String] { text = conf.getString("owner-table-column-since"); cellValueFactory = { _.value.sinceProperty } },
      new TableColumn[Owner, String] { text = conf.getString("owner-table-column-first"); cellValueFactory = { _.value.firstProperty } },
      new TableColumn[Owner, String] { text = conf.getString("owner-table-column-last"); cellValueFactory = { _.value.lastroperty } },
      new TableColumn[Owner, String] { text = conf.getString("owner-table-column-email"); cellValueFactory = { _.value.emailProperty } }
    )
    prefHeight = 100
    items = model.ownerList
  }
  ownerTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val ownerAddButton = new Button { graphic = Images.addImageView() }
  val ownerEditButton = new Button { graphic = Images.editImageView(); disable = true }
  val ownerToolBar = new HBox { spacing = 6; children = List(ownerAddButton, ownerEditButton) }

  spacing = 6
  children = List(ownerLabel, ownerTableView, ownerToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listOwners(selectedPoolId.intValue)
    ownerAddButton.disable = false
  }

  ownerTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedOwner) =>
    // model.update executes a remove and add on items. the remove passes a null selectedOwner!
    if (selectedOwner != null) {
      model.selectedOwnerId.value = selectedOwner.id
      ownerEditButton.disable = false
    }
  }

  ownerTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && ownerTableView.selectionModel().getSelectedItem != null ) update()
  }

  ownerAddButton.onAction = { _ => add() }

  ownerEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new OwnerDialog(conf, Owner(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Owner(id, poolId, since, first, last, email)) =>
        val newOwner = model.addOwner(Owner(id, poolId, since, first, last, email))
        ownerTableView.selectionModel().select(newOwner)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = ownerTableView.selectionModel().getSelectedIndex
    val owner = ownerTableView.selectionModel().getSelectedItem.owner
    new OwnerDialog(conf, owner).showAndWait() match {
      case Some(Owner(id, poolId, since, first, last, email)) =>
        model.updateOwner(selectedIndex, Owner(id, poolId, since, first, last, email))
        ownerTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}