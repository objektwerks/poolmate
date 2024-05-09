package poolmate.dialog

import com.typesafe.config.Config

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import poolmate.{App, Entity, Heater}
import poolmate.pane.ControlGridPane

class HeaterDialog(conf: Config, heater: Heater) extends Dialog[Heater]:
  val installedDatePicker = new DatePicker:
    value = Entity.toLocalDate(heater.installed)

  val modelTextField = new TextField:
    text = heater.model

  val controls = List[(String, Region)](
    conf.getString("heater-installed") -> installedDatePicker,
    conf.getString("heater-model") -> modelTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  modelTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      heater.copy(
        installed = installedDatePicker.value.value.toString,
        model = modelTextField.text.value
      )
    else null

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-heater")