package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.Resources._
import objektwerks.poolmate.dialog.{SupplyChartDialog, SupplyDialog}
import objektwerks.poolmate.entity.Supply
import objektwerks.poolmate.model.Model
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class SupplyPane(conf: Config, model: Model) extends VBox {
  val supplyTableView = new TableView[Supply]() {
    columns ++= List(
      new TableColumn[Supply, String] {
        text = conf.getString("supply-header-purchased"); cellValueFactory = {
          _.value.purchasedProperty
        }
      },
      new TableColumn[Supply, String] {
        text = conf.getString("supply-header-item"); cellValueFactory = {
          _.value.itemProperty
        }
      },
      new TableColumn[Supply, String] {
        text = conf.getString("supply-header-unit"); cellValueFactory = {
          _.value.unitProperty
        }
      },
      new TableColumn[Supply, String] {
        text = conf.getString("supply-header-amount"); cellValueFactory = {
          _.value.amountProperty
        }
      },
      new TableColumn[Supply, String] {
        text = conf.getString("supply-header-cost"); cellValueFactory = {
          _.value.costProperty
        }
      }
    )
    prefHeight = conf.getInt("height").toDouble
    items = model.supplyList
  }
  supplyTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val supplyAddButton = new Button {
    graphic = addImageView(); disable = true
  }
  val supplyEditButton = new Button {
    graphic = editImageView(); disable = true
  }
  val supplyChartButton = new Button {
    graphic = barChartImageView(); disable = true
  }
  val supplyToolBar = new HBox {
    spacing = 6; children = List(supplyAddButton, supplyEditButton, supplyChartButton)
  }

  spacing = 6
  padding = Insets(6)
  children = List(supplyTableView, supplyToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listSupplies(selectedPoolId.intValue)
    supplyAddButton.disable = false
    supplyChartButton.disable = if (model.supplyList.nonEmpty) false else true
  }

  supplyTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedSupply) =>
    // model.update executes a remove and add on items. the remove passes a null selectedSupply!
    if (selectedSupply != null) {
      model.selectedSupplyId.value = selectedSupply.id
      supplyEditButton.disable = false
      supplyChartButton.disable = false
    }
  }

  supplyTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 && supplyTableView.selectionModel().getSelectedItem != null) update()
  }

  supplyAddButton.onAction = { _ => add() }

  supplyEditButton.onAction = { _ => update() }

  supplyChartButton.onAction = { _ =>
    new SupplyChartDialog(conf, model).showAndWait()
    ()
  }

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