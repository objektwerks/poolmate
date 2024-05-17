package poolmate.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, VBox}

import poolmate.{Context, Measurement, Model}
import poolmate.dialog.{MeasurementChartDialog, MeasurementDialog}

class MeasurementPane(context: Context, model: Model) extends VBox:
  val measurementTableView = new TableView[Measurement]:
    columns ++= List(
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderOn
        cellValueFactory = {
          _.value.onProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderTemp
        cellValueFactory = {
          _.value.tempProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderHardness
        cellValueFactory = {
          _.value.hardnessProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderTotalChlorine
        cellValueFactory = {
          _.value.totalChlorineProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderBromine
        cellValueFactory = {
          _.value.bromineProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderFreeChlorine
        cellValueFactory = {
          _.value.freeChlorineProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderPh
        cellValueFactory = {
          _.value.pHProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderAlkalinity
        cellValueFactory = {
          _.value.alkalinityProperty
        }
      ,
      new TableColumn[Measurement, String]:
        text = context.measurementHeaderCyanuricAcid
        cellValueFactory = {
          _.value.cyanuricAcidProperty
        }
    )
    prefHeight = context.height.toDouble
    items = model.measurementList
  measurementTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  val measurementAddButton = new Button:
    graphic = context.addImageView
    disable = true

  val measurementEditButton = new Button:
    graphic = context.editImageView
    disable = true

  val measurementChartButton = new Button:
    graphic = context.lineChartImageView
    disable = true

  val measurementToolBar = new HBox:
    spacing = 6
    children = List(measurementAddButton, measurementEditButton, measurementChartButton)

  spacing = 6
  padding = Insets(6)
  children = List(measurementTableView, measurementToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listMeasurements(selectedPoolId.intValue)
    measurementAddButton.disable = false
    measurementChartButton.disable = if (model.measurementList.nonEmpty) then false else true
  }

  measurementTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedMeasurement) =>
    // model.update executes a remove and add on items. the remove passes a null selectedMeasurement!
    if (selectedMeasurement != null) then
      model.selectedMeasurementId.value = selectedMeasurement.id
      measurementEditButton.disable = false
      measurementChartButton.disable = false
  }

  measurementTableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 &&
        measurementTableView.selectionModel().getSelectedItem != null) then update()
  }

  measurementAddButton.onAction = { _ => add() }

  measurementEditButton.onAction = { _ => update() }

  measurementChartButton.onAction = { _ => MeasurementChartDialog(context, model).showAndWait() }

  def add(): Unit =
    MeasurementDialog(context, Measurement(poolId = model.selectedPoolId.toInt)).showAndWait() match
      case Some(Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)) =>
        val newMeasurement = model.addMeasurement(
          Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)
        )
        measurementTableView.selectionModel().select(newMeasurement)
      case _ =>

  def update(): Unit =
    val selectedIndex = measurementTableView.selectionModel().getSelectedIndex
    val measurement = measurementTableView.selectionModel().getSelectedItem.measurement
    MeasurementDialog(context, measurement).showAndWait() match
      case Some(Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)) =>
        model.updateMeasurement(
          selectedIndex,
          Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)
        )
        measurementTableView.selectionModel().select(selectedIndex)
      case _ =>