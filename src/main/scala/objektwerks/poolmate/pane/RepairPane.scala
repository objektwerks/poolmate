package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.RepairDialog
import objektwerks.poolmate.entity.Repair
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class RepairPane(conf: Config, model: Model) extends VBox {
  val repairTableView = new TableView[Repair]() {
    columns ++= List(
      new TableColumn[Repair, String] { text = conf.getString("repair-table-column-on"); cellValueFactory = { _.value.onProperty } },
      new TableColumn[Repair, String] { text = conf.getString("repair-table-column-item"); cellValueFactory = { _.value.itemProperty } },
      new TableColumn[Repair, String] { text = conf.getString("repair-table-column-cost"); cellValueFactory = { _.value.costProperty } }
    )
    items = model.repairList
  }
  repairTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val repairAddButton = new Button { graphic = Images.addImageView() }
  val repairEditButton = new Button { graphic = Images.editImageView(); disable = true }
  val repairToolBar = new HBox { spacing = 6; children = List(repairAddButton, repairEditButton) }

  spacing = 6
  children = List(repairTableView, repairToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listRepairs(selectedPoolId.intValue)
    repairAddButton.disable = false
  }

  repairTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedRepair) =>
    // model.update executes a remove and add on items. the remove passes a null selectedRepair!
    if (selectedRepair != null) {
      model.selectedRepairId.value = selectedRepair.id
      repairEditButton.disable = false
    }
  }

  repairTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && repairTableView.selectionModel().getSelectedItem != null ) update()
  }

  repairAddButton.onAction = { _ => add() }

  repairEditButton.onAction = { _ => update() }

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