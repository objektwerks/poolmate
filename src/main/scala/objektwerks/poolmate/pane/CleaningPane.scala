package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.CleaningDialog
import objektwerks.poolmate.entity.Cleaning
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class CleaningPane(conf: Config, model: Model) extends VBox {
  val cleaningTableView = new TableView[Cleaning]() {
    columns ++= List(
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-on"); cellValueFactory = { _.value.onProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-deck"); cellValueFactory = { _.value.deckProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-brush"); cellValueFactory = { _.value.brushProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-net"); cellValueFactory = { _.value.netProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-vacuum"); cellValueFactory = { _.value.vacuumProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-skimmer-basket"); cellValueFactory = { _.value.skimmerBasketProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-pump-basket"); cellValueFactory = { _.value.pumpBasketProperty } },
      new TableColumn[Cleaning, String] { text = conf.getString("cleaning-table-column-pump-filter"); cellValueFactory = { _.value.pumpFilterProperty } }
    )
    items = model.cleaningList
  }
  cleaningTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val cleaningAddButton = new Button { graphic = Images.addImageView() }
  val cleaningEditButton = new Button { graphic = Images.editImageView(); disable = true }
  val cleaningToolBar = new HBox { spacing = 6; children = List(cleaningAddButton, cleaningEditButton) }

  spacing = 6
  children = List(cleaningTableView, cleaningToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listCleanings(selectedPoolId.intValue)
    cleaningAddButton.disable = false
  }

  cleaningTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedCleaning) =>
    // model.update executes a remove and add on items. the remove passes a null selectedCleaning!
    if (selectedCleaning != null) {
      model.selectedCleaningId.value = selectedCleaning.id
      cleaningEditButton.disable = false
    }
  }

  cleaningTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && cleaningTableView.selectionModel().getSelectedItem != null ) update()
  }

  cleaningAddButton.onAction = { _ => add() }

  cleaningEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new CleaningDialog(conf, Cleaning(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter)) =>
        val newCleaning = model.addCleaning(Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter))
        cleaningTableView.selectionModel().select(newCleaning)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = cleaningTableView.selectionModel().getSelectedIndex
    val cleaning = cleaningTableView.selectionModel().getSelectedItem.cleaning
    new CleaningDialog(conf, cleaning).showAndWait() match {
      case Some(Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter)) =>
        model.updateCleaning(selectedIndex, Cleaning(id, poolId, on, deck, brush, net, vacuum, skimmerBasket, pumpBasket, pumpFilter))
        cleaningTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}