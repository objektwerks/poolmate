package poolmate.dialog

import com.typesafe.config.Config

import poolmate.{App, Heater}
import poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class HeaterDialog(conf: Config, heater: Heater) extends Dialog[Heater] {
  val installedDatePicker = new DatePicker {
    value = heater.installed
  }
  val modelTextField = new TextField {
    text = heater.model
  }
  val controls = List[(String, Region)](
    conf.getString("heater-installed") -> installedDatePicker,
    conf.getString("heater-model") -> modelTextField
  )
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  modelTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      heater.copy(installed = installedDatePicker.value.value, model = modelTextField.text.value)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-heater")
}