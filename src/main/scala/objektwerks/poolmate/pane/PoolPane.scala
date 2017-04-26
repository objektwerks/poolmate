package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.PoolDialog
import objektwerks.poolmate.entity.Pool
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class PoolPane(conf: Config, model: Model) extends VBox  {
  val poolLabel = new Label { text = conf.getString("pools") }
  val poolTableView = new TableView[Pool]() {
    columns ++= List(
      new TableColumn[Pool, String] { text = conf.getString("pool-table-column-built"); cellValueFactory = { _.value.pBuilt } },
      new TableColumn[Pool, String] { text = conf.getString("pool-table-column-gallons"); cellValueFactory = { _.value.pGallons } },
      new TableColumn[Pool, String] { text = conf.getString("pool-table-column-city"); cellValueFactory = { _.value.pCity } }
    )
  }
  poolTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val poolAddButton = new Button { graphic = Images.addImageView(); prefHeight = 25 }
  val poolEditButton = new Button { graphic = Images.editImageView(); prefHeight = 25; disable = true }
  val poolToolBar = new HBox { spacing = 6; children = List(poolAddButton, poolEditButton) }

  spacing = 6
  children = List(poolLabel, poolToolBar)

  poolTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedPool) =>
    // model.update executes a remove and add on items. the remove passes a null selectedPool!
    if (selectedPool != null) {
      model.selectedPoolId.value = selectedPool.pId.value
      poolEditButton.disable = false
    }
  }

  poolTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && poolTableView.selectionModel().getSelectedItem != null ) update()
  }

  poolAddButton.onAction = { _ => add() }

  poolEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new PoolDialog(conf, Pool()).showAndWait() match {
      case Some(Pool(id, built, gallons, street, city, state, zip)) =>
        val newPool = model.addPool(Pool(id, built, gallons, street, city, state, zip))
        poolTableView.selectionModel().select(newPool)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = poolTableView.selectionModel().getSelectedIndex
    val pool = poolTableView.selectionModel().getSelectedItem.pool
    new PoolDialog(conf, pool).showAndWait() match {
      case Some(Pool(id, built, gallons, street, city, state, zip)) =>
        model.updatePool(selectedIndex, Pool(id, built, gallons, street, city, state, zip))
        poolTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}