package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.dialog.MeasurementDialog
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.image.Images
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class MeasurementPane(conf: Config, model: Model) extends VBox {
  val measurementLabel = new Label { text = conf.getString("measurements") }
  val measurementTableView = new TableView[Measurement]() {
    columns ++= List(
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-on"); cellValueFactory = { _.value.onProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-temp"); cellValueFactory = { _.value.tempProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-hardness"); cellValueFactory = { _.value.hardnessProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-total-chlorine"); cellValueFactory = { _.value.totalChlorineProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-bromine"); cellValueFactory = { _.value.bromineProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-free-chlorine"); cellValueFactory = { _.value.freeChlorineProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-ph"); cellValueFactory = { _.value.pHProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-alkalinity"); cellValueFactory = { _.value.alkalinityProperty } },
      new TableColumn[Measurement, String] { text = conf.getString("measurement-table-column-cyanuric-acid"); cellValueFactory = { _.value.cyanuricAcidProperty } }
    )
    prefHeight = 100
    items = model.measurementList
  }
  measurementTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val measurementAddButton = new Button { graphic = Images.addImageView() }
  val measurementEditButton = new Button { graphic = Images.editImageView(); disable = true }
  val measurementToolBar = new HBox { spacing = 6; children = List(measurementAddButton, measurementEditButton) }

  spacing = 6
  children = List(measurementLabel, measurementTableView, measurementToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listMeasurements(selectedPoolId.intValue)
    measurementAddButton.disable = false
  }

  measurementTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedMeasurement) =>
    // model.update executes a remove and add on items. the remove passes a null selectedMeasurement!
    if (selectedMeasurement != null) {
      model.selectedMeasurementId.value = selectedMeasurement.id
      measurementEditButton.disable = false
    }
  }

  measurementTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && measurementTableView.selectionModel().getSelectedItem != null ) update()
  }

  measurementAddButton.onAction = { _ => add() }

  measurementEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new MeasurementDialog(conf, Measurement(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)) =>
        val newMeasurement = model.addMeasurement(Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid))
        measurementTableView.selectionModel().select(newMeasurement)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = measurementTableView.selectionModel().getSelectedIndex
    val measurement = measurementTableView.selectionModel().getSelectedItem.measurement
    new MeasurementDialog(conf, measurement).showAndWait() match {
      case Some(Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)) =>
        model.updateMeasurement(selectedIndex, Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid))
        measurementTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}