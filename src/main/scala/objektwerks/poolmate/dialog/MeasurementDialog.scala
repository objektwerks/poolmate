package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class MeasurementDialog(conf: Config, measurement: Measurement) extends Dialog[Measurement]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val onDatePicker = new DatePicker { value = measurement.on }
  val tempSlider = new Slider { prefWidth = 600; min = 0.0; max = 100.0; majorTickUnit = 10.0; showTickLabels = true; showTickMarks = true; value = measurement.temp }
  val hardnessSlider = new Slider { prefWidth = 600; min = 0.0; max = 1000.0; majorTickUnit = 100.0; showTickLabels = true; showTickMarks = true; value = measurement.hardness }
  val totalChlorineSlider = new Slider { prefWidth = 600; min = 0.0; max = 10.0; majorTickUnit = 1.0; showTickLabels = true; showTickMarks = true; value = measurement.totalChlorine }
  val bromineSlider = new Slider { prefWidth = 600; min = 0.0; max = 20.0; majorTickUnit = 1.0; showTickLabels = true; showTickMarks = true; value = measurement.bromine }
  val freeChlorineSlider = new Slider { prefWidth = 600; min = 0.0; max = 10.0;; majorTickUnit = 1.0; showTickLabels = true; showTickMarks = true; value = measurement.freeChlorine }
  val phSlider = new Slider { prefWidth = 600; min = 6.2; max = 8.4; majorTickUnit = 0.2; showTickLabels = true; showTickMarks = true; value = measurement.pH }
  val alkalinitySlider = new Slider { prefWidth = 600; prefWidth = 200; min = 0.0; max = 240; majorTickUnit = 20.0; showTickMarks = true; showTickLabels = true; value = measurement.alkalinity }
  val cyanuricAcidSlider = new Slider { prefWidth = 600; min = 0.0; max = 300.0; majorTickUnit = 25.0; showTickLabels = true; showTickMarks = true; value = measurement.cyanuricAcid }
  val controls = List[(String, Region)](
    conf.getString("measurement-on") -> onDatePicker,
    conf.getString("measurement-temp") -> tempSlider,
    conf.getString("measurement-hardness") -> hardnessSlider,
    conf.getString("measurement-total-chlorine") -> totalChlorineSlider,
    conf.getString("measurement-bromine") -> bromineSlider,
    conf.getString("measurement-free-chlorine") -> freeChlorineSlider,
    conf.getString("measurement-ph") -> phSlider,
    conf.getString("measurement-alkalinity") -> alkalinitySlider,
    conf.getString("measurement-cyanuric-acid") -> cyanuricAcidSlider
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-measurement")

  val saveButton = dialog.lookupButton(saveButtonType)
  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      measurement.copy(on = onDatePicker.value.value,
                       temp = tempSlider.value.toInt,
                       hardness = hardnessSlider.value.toInt,
                       totalChlorine = totalChlorineSlider.value.toInt,
                       bromine = bromineSlider.value.toInt,
                       freeChlorine = freeChlorineSlider.value.toInt,
                       pH = phSlider.value.toDouble,
                       alkalinity = alkalinitySlider.value.toInt,
                       cyanuricAcid = cyanuricAcidSlider.value.toInt)
    else null
  }
}