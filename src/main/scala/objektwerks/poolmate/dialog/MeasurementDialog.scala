package objektwerks.poolmate.dialog

import java.text.DecimalFormat

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.pane.ControlGridPane
import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, Region}

class MeasurementDialog(conf: Config, measurement: Measurement) extends Dialog[Measurement]() {
  val doubleFormatter = new DecimalFormat("#.00")

  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val onDatePicker = new DatePicker { value = measurement.on }

  val tempSlider = new Slider { prefWidth = 600; min = 0; max = 100; majorTickUnit = 10; showTickLabels = true; showTickMarks = true; value = measurement.temp }
  val tempLabel = new Label { text = measurement.temp.toString }
  val tempControl = new HBox { spacing = 3; children = List(tempSlider, tempLabel) }

  val hardnessSlider = new Slider { prefWidth = 600; min = 0; max = 1000; majorTickUnit = 100; showTickLabels = true; showTickMarks = true; value = measurement.hardness }
  val hardnessLabel = new Label { text = measurement.hardness.toString }
  val hardnessControl = new HBox { spacing = 3; children = List(hardnessSlider, hardnessLabel) }

  val totalChlorineSlider = new Slider { prefWidth = 600; min = 0; max = 10; majorTickUnit = 1; showTickLabels = true; showTickMarks = true; value = measurement.totalChlorine }
  val totalChlorineLabel = new Label { text = measurement.totalChlorine.toString }
  val totalChlorineControl = new HBox { spacing = 3; children = List(totalChlorineSlider, totalChlorineLabel) }

  val bromineSlider = new Slider { prefWidth = 600; min = 0.0; max = 20; majorTickUnit = 1; showTickLabels = true; showTickMarks = true; value = measurement.bromine }
  val bromineLabel = new Label { text = measurement.bromine.toString }
  val bromineControl = new HBox { spacing = 3; children = List(bromineSlider, bromineLabel) }

  val freeChlorineSlider = new Slider { prefWidth = 600; min = 0; max = 10; majorTickUnit = 1; showTickLabels = true; showTickMarks = true; value = measurement.freeChlorine }
  val freeChlorineLabel = new Label { text = measurement.freeChlorine.toString }
  val freeChlorineControl = new HBox { spacing = 3; children = List(freeChlorineSlider, freeChlorineLabel) }

  val phSlider = new Slider { prefWidth = 600; min = 6.2; max = 8.4; majorTickUnit = 0.2; showTickLabels = true; showTickMarks = true; value = measurement.pH }
  val phLabel = new Label { text = doubleFormatter.format(measurement.pH) }
  val phControl = new HBox { spacing = 3; children = List(phSlider, phLabel) }

  val alkalinitySlider = new Slider { prefWidth = 600; min = 0; max = 240; majorTickUnit = 20; showTickMarks = true; showTickLabels = true; value = measurement.alkalinity }
  val alkalinityLabel = new Label { text = measurement.alkalinity.toString }
  val alkalinityControl = new HBox { spacing = 3; children = List(alkalinitySlider, alkalinityLabel) }

  val cyanuricAcidSlider = new Slider { prefWidth = 600; min = 0; max = 300; majorTickUnit = 25; showTickLabels = true; showTickMarks = true; value = measurement.cyanuricAcid }
  val cyanuricAcidLabel = new Label { text = measurement.temp.toString }
  val cyanuricAcidControl = new HBox { spacing = 3; children = List(cyanuricAcidSlider, cyanuricAcidLabel) }

  val controls = List[(String, Region)](
    conf.getString("measurement-on") -> onDatePicker,
    conf.getString("measurement-temp") -> tempControl,
    conf.getString("measurement-hardness") -> hardnessControl,
    conf.getString("measurement-total-chlorine") -> totalChlorineControl,
    conf.getString("measurement-bromine") -> bromineControl,
    conf.getString("measurement-free-chlorine") -> freeChlorineControl,
    conf.getString("measurement-ph") -> phControl,
    conf.getString("measurement-alkalinity") -> alkalinityControl,
    conf.getString("measurement-cyanuric-acid") -> cyanuricAcidControl
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-measurement")

  tempSlider.value.onChange { (_, _, newValue) => tempLabel.text = newValue.intValue.toString }
  hardnessSlider.value.onChange { (_, _, newValue) => hardnessLabel.text = newValue.intValue.toString }
  totalChlorineSlider.value.onChange { (_, _, newValue) => totalChlorineLabel.text = newValue.intValue.toString }
  bromineSlider.value.onChange { (_, _, newValue) => bromineLabel.text = newValue.intValue.toString }
  freeChlorineSlider.value.onChange { (_, _, newValue) => freeChlorineLabel.text = newValue.intValue.toString }
  phSlider.value.onChange { (_, _, newValue) => phLabel.text = doubleFormatter.format(newValue.doubleValue()) }
  alkalinitySlider.value.onChange { (_, _, newValue) => alkalinityLabel.text = newValue.intValue.toString }
  cyanuricAcidSlider.value.onChange { (_, _, newValue) => cyanuricAcidLabel.text = newValue.intValue.toString }

  val saveButton = dialog.lookupButton(saveButtonType)
  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      measurement.copy(on = onDatePicker.value.value,
                       temp = tempSlider.value.get,
                       hardness = hardnessSlider.value.get,
                       totalChlorine = totalChlorineSlider.value.get,
                       bromine = bromineSlider.value.get,
                       freeChlorine = freeChlorineSlider.value.get,
                       pH = phLabel.text.value.toDouble,
                       alkalinity = alkalinitySlider.value.get,
                       cyanuricAcid = cyanuricAcidSlider.value.get)
    else null
  }
}