package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.SupplyDialog
import objektwerks.poolmate.entity.Supply
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class SupplyPane(conf: Config, model: Model) extends VBox {
  val supplyLabel = new Label { text = conf.getString("supplies") }
  val supplyTableView = new TableView[Supply]() {
    columns ++= List(
      new TableColumn[Supply, String] { text = conf.getString("supply-table-column-purchased"); cellValueFactory = { _.value.purchasedProperty } },
      new TableColumn[Supply, String] { text = conf.getString("supply-table-column-item"); cellValueFactory = { _.value.itemProperty } },
      new TableColumn[Supply, String] { text = conf.getString("supply-table-column-unit"); cellValueFactory = { _.value.unitProperty } },
      new TableColumn[Supply, String] { text = conf.getString("supply-table-column-amount"); cellValueFactory = { _.value.amountProperty } },
      new TableColumn[Supply, String] { text = conf.getString("supply-table-column-cost"); cellValueFactory = { _.value.costProperty } }
    )
    items = model.supplyList
  }
  supplyTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val supplyAddButton = new Button { graphic = Images.addImageView() }
  val supplyEditButton = new Button { graphic = Images.editImageView(); disable = true }
  val supplyToolBar = new HBox { spacing = 6; children = List(supplyAddButton, supplyEditButton) }

  spacing = 6
  children = List(supplyLabel, supplyTableView, supplyToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listSupplies(selectedPoolId.intValue)
    supplyAddButton.disable = false
  }

  supplyTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedSupply) =>
    // model.update executes a remove and add on items. the remove passes a null selectedSupply!
    if (selectedSupply != null) {
      model.selectedSupplyId.value = selectedSupply.id
      supplyEditButton.disable = false
    }
  }

  supplyTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && supplyTableView.selectionModel().getSelectedItem != null ) update()
  }

  supplyAddButton.onAction = { _ => add() }

  supplyEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new SupplyDialog(conf, Supply(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Supply(id, poolId, purchased, item, unit, amount, cost)) =>
        val newSupply = model.addSupply(Supply(id, poolId, purchased, item, unit, amount, cost))
        supplyTableView.selectionModel().select(newSupply)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = supplyTableView.selectionModel().getSelectedIndex
    val supply = supplyTableView.selectionModel().getSelectedItem.supply
    new SupplyDialog(conf, supply).showAndWait() match {
      case Some(Supply(id, poolId, purchased, item, unit, amount, cost)) =>
        model.updateSupply(selectedIndex, Supply(id, poolId, purchased, item, unit, amount, cost))
        supplyTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}