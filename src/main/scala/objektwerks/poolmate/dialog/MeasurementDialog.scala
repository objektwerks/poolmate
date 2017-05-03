package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class MeasurementDialog(conf: Config, measurement: Measurement) extends Dialog[Measurement]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val onDatePicker = new DatePicker { value = measurement.on }
  val tempTextField = new TextField { text = measurement.temp.toString }
  val hardnessTextField = new TextField { text = measurement.hardness.toString }
  val totalChlorineTextField = new TextField { text = measurement.totalChlorine.toString }
  val bromineTextField = new TextField { text = measurement.bromine.toString }
  val freeChlorineTextField = new TextField { text = measurement.freeChlorine.toString }
  val pHTextField = new TextField { text = measurement.pH.toString }
  val alkalinityTextField = new TextField { text = measurement.alkalinity.toString }
  val cyanuricAcidTextField = new TextField { text = measurement.cyanuricAcid.toString }
  val controls = List[(String, Region)](
    conf.getString("measurement-on") -> onDatePicker,
    conf.getString("measurement-temp") -> tempTextField,
    conf.getString("measurement-hardness") -> hardnessTextField,
    conf.getString("measurement-total-chlorine") -> totalChlorineTextField,
    conf.getString("measurement-bromine") -> bromineTextField,
    conf.getString("measurement-free-chlorine") -> freeChlorineTextField,
    conf.getString("measurement-ph") -> pHTextField,
    conf.getString("measurement-alkalinity") -> alkalinityTextField,
    conf.getString("measurement-cyanuric-acid") -> cyanuricAcidTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-measurement")

  val saveButton = dialog.lookupButton(saveButtonType)

  val isNotInt = (text: String) => !text.matches("[0-9]*")
  tempTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) tempTextField.text.value = oldValue }
  hardnessTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) hardnessTextField.text.value = oldValue }
  totalChlorineTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) totalChlorineTextField.text.value = oldValue }
  bromineTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) bromineTextField.text.value = oldValue }
  freeChlorineTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) freeChlorineTextField.text.value = oldValue }
  alkalinityTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) alkalinityTextField.text.value = oldValue }
  cyanuricAcidTextField.text.onChange { (_, oldValue, newValue) => if (isNotInt(newValue)) cyanuricAcidTextField.text.value = oldValue }

  val isNotDouble = (text: String) => !text.matches("[0-9]{1,13}(\\.[0-9]+)?")
  pHTextField.text.onChange { (_, oldValue, newValue) => if (isNotDouble(newValue)) pHTextField.text.value = oldValue }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      measurement.copy(on = onDatePicker.value.value,
                       temp = tempTextField.text.value.toInt,
                       hardness = hardnessTextField.text.value.toInt,
                       totalChlorine = totalChlorineTextField.text.value.toInt,
                       bromine = bromineTextField.text.value.toInt,
                       freeChlorine = freeChlorineTextField.text.value.toInt,
                       pH = pHTextField.text.value.toDouble,
                       alkalinity = alkalinityTextField.text.value.toInt,
                       cyanuricAcid = cyanuricAcidTextField.text.value.toInt)
    else null
  }
}