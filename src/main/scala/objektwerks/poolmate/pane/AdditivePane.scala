package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.AdditiveDialog
import objektwerks.poolmate.entity.Additive
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class AdditivePane(conf: Config, model: Model) extends VBox {
  val additiveTableView = new TableView[Additive]() {
    columns ++= List(
      new TableColumn[Additive, String] { text = conf.getString("additive-table-column-on"); cellValueFactory = { _.value.onProperty } },
      new TableColumn[Additive, String] { text = conf.getString("additive-table-column-chemical"); cellValueFactory = { _.value.chemicalProperty } },
      new TableColumn[Additive, String] { text = conf.getString("additive-table-column-unit"); cellValueFactory = { _.value.unitProperty } },
      new TableColumn[Additive, String] { text = conf.getString("additive-table-column-amount"); cellValueFactory = { _.value.amountProperty } }
    )
    items = model.additiveList
  }
  additiveTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val additiveAddButton = new Button { graphic = Images.addImageView() }
  val additiveEditButton = new Button { graphic = Images.editImageView(); disable = true }
  val additiveToolBar = new HBox { spacing = 6; children = List(additiveAddButton, additiveEditButton) }

  spacing = 6
  children = List(additiveTableView, additiveToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listAdditives(selectedPoolId.intValue)
    additiveAddButton.disable = false
  }

  additiveTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedAdditive) =>
    // model.update executes a remove and add on items. the remove passes a null selectedAdditive!
    if (selectedAdditive != null) {
      model.selectedAdditiveId.value = selectedAdditive.id
      additiveEditButton.disable = false
    }
  }

  additiveTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && additiveTableView.selectionModel().getSelectedItem != null ) update()
  }

  additiveAddButton.onAction = { _ => add() }

  additiveEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new AdditiveDialog(conf, Additive(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Additive(id, poolId, on, chemical, unit, amount)) =>
        val newAdditive = model.addAdditive(Additive(id, poolId, on, chemical, unit, amount))
        additiveTableView.selectionModel().select(newAdditive)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = additiveTableView.selectionModel().getSelectedIndex
    val additive = additiveTableView.selectionModel().getSelectedItem.additive
    new AdditiveDialog(conf, additive).showAndWait() match {
      case Some(Additive(id, poolId, on, chemical, unit, amount)) =>
        model.updateAdditive(selectedIndex, Additive(id, poolId, on, chemical, unit, amount))
        additiveTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}