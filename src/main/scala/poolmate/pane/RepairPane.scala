package poolmate.pane

import com.typesafe.config.Config

import poolmate.Resources._
import poolmate.dialog.{RepairChartDialog, RepairDialog}
import poolmate.Repair
import poolmate.Model

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class RepairPane(conf: Config, model: Model) extends VBox {
  val repairTableView = new TableView[Repair]() {
    columns ++= List(
      new TableColumn[Repair, String] {
        text = conf.getString("repair-header-on"); cellValueFactory = {
          _.value.onProperty
        }
      },
      new TableColumn[Repair, String] {
        text = conf.getString("repair-header-item"); cellValueFactory = {
          _.value.itemProperty
        }
      },
      new TableColumn[Repair, String] {
        text = conf.getString("repair-header-cost"); cellValueFactory = {
          _.value.costProperty
        }
      }
    )
    prefHeight = conf.getInt("height").toDouble
    items = model.repairList
  }
  repairTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val repairAddButton = new Button {
    graphic = addImageView
    disable = true
  }
  val repairEditButton = new Button {
    graphic = editImageView
    disable = true
  }
  val repairChartButton = new Button {
    graphic = lineChartImageView
    disable = true
  }
  val repairToolBar = new HBox {
    spacing = 6; children = List(repairAddButton, repairEditButton, repairChartButton)
  }

  spacing = 6
  padding = Insets(6)
  children = List(repairTableView, repairToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listRepairs(selectedPoolId.intValue)
    repairAddButton.disable = false
    repairChartButton.disable = if (model.repairList.nonEmpty) false else true
  }

  repairTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedRepair) =>
    // model.update executes a remove and add on items. the remove passes a null selectedRepair!
    if (selectedRepair != null) {
      model.selectedRepairId.value = selectedRepair.id
      repairEditButton.disable = false
      repairChartButton.disable = false
    }
  }

  repairTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 && repairTableView.selectionModel().getSelectedItem != null) update()
  }

  repairAddButton.onAction = { _ => add() }

  repairEditButton.onAction = { _ => update() }

  repairChartButton.onAction = { _ =>
    new RepairChartDialog(conf, model).showAndWait()
    ()
  }

  def add(): Unit = {
    new RepairDialog(conf, Repair(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Repair(id, poolId, on, item, cost)) =>
        val newRepair = model.addRepair(Repair(id, poolId, on, item, cost))
        repairTableView.selectionModel().select(newRepair)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = repairTableView.selectionModel().getSelectedIndex
    val repair = repairTableView.selectionModel().getSelectedItem.repair
    new RepairDialog(conf, repair).showAndWait() match {
      case Some(Repair(id, poolId, on, item, cost)) =>
        model.updateRepair(selectedIndex, Repair(id, poolId, on, item, cost))
        repairTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}