package objektwerks.poolmate.pane

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.PoolDialog
import objektwerks.poolmate.entity.Pool
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.control.{Button, Label, TableColumn, TableView}
import scalafx.scene.control.TableColumn._
import scalafx.scene.layout.{HBox, VBox}

class PoolProperty(pool: Pool) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val id = new IntegerProperty(this, "id", pool.id)
  val built = new StringProperty(this, "built", pool.built.format(dateFormatter))
  val gallons = new StringProperty(this, "gallons", pool.gallons.toString)
  val street = new StringProperty(this, "street", pool.street)
  val city = new StringProperty(this, "city", pool.city)
  val state = new StringProperty(this, "state", pool.state)
  val zip = new StringProperty(this, "zip", pool.zip.toString)

  val value = pool
}

class PoolPane(conf: Config, model: Model) extends VBox  {
  val poolLabel = new Label { text = conf.getString("pools") }
  val poolTableView = new TableView[PoolProperty]() {
    columns ++= List(
      new TableColumn[PoolProperty, String] { text = "Built"; cellValueFactory = { _.value.built } },
      new TableColumn[PoolProperty, String] { text = "Gallons"; cellValueFactory = { _.value.gallons } },
      new TableColumn[PoolProperty, String] { text = "City"; cellValueFactory = { _.value.city } }
    )
  }
  val poolAddButton = new Button { graphic = Images.addImageView(); prefHeight = 25 }
  val poolEditButton = new Button { graphic = Images.editImageView(); prefHeight = 25; disable = true }
  val poolToolBar = new HBox { spacing = 6; children = List(poolAddButton, poolEditButton) }

  spacing = 6
  children = List(poolLabel, poolToolBar)

  poolTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedPool) =>
    // model.update executes a remove and add on items. the remove passes a null selectedPool!
    if (selectedPool != null) {
      model.selectedPoolId.value = selectedPool.id.value
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
        poolTableView.selectionModel().select(new PoolProperty(newPool))
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = poolTableView.selectionModel().getSelectedIndex
    val pool = poolTableView.selectionModel().getSelectedItem.value
    new PoolDialog(conf, pool).showAndWait() match {
      case Some(Pool(id, built, gallons, street, city, state, zip)) =>
        model.updatePool(selectedIndex, Pool(id, built, gallons, street, city, state, zip))
        poolTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}