package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import poolmate.{App, Context, Entity, Pump}
import poolmate.pane.ControlGridPane

class PumpDialog(context: Context, pump: Pump) extends Dialog[Pump]:
  val installedDatePicker = new DatePicker:
    value = Entity.toLocalDate(pump.installed)

  val modelTextField = new TextField:
    text = pump.model

  val controls = List[(String, Region)](
    context.pumpInstalled -> installedDatePicker,
    context.pumpModel -> modelTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  modelTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      pump.copy(
        installed = installedDatePicker.value.value.toString,
        model = modelTextField.text.value
      )
    else null

  initOwner(App.stage)
  title = context.title
  headerText = context.savePump 