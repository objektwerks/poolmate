package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import Dialogs.*
import poolmate.{App, Context, Entity, Pool}
import poolmate.pane.ControlGridPane

class PoolDialog(context: Context, pool: Pool) extends Dialog[Pool]:
  val builtDatePicker = new DatePicker:
    value = Entity.toLocalDate(pool.built)

  val gallonsTextField = new TextField:
    text = pool.gallons.toString

  val streetTextField = new TextField:
    text = pool.street

  val cityTextField = new TextField:
    text = pool.city

  val stateTextField = new TextField:
    text = pool.state

  val zipTextField = new TextField:
    text = pool.zip.toString

  val controls = List[(String, Region)](
    context.poolBuilt -> builtDatePicker,
    context.poolGallons -> gallonsTextField,
    context.poolStreet -> streetTextField,
    context.poolCity -> cityTextField,
    context.poolState -> stateTextField,
    context.poolZip -> zipTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  gallonsTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) then gallonsTextField.text.value = oldValue }
  streetTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  cityTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  stateTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  zipTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) then zipTextField.text.value = oldValue }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      pool.copy(
        built = builtDatePicker.value.value.toString,
        gallons = Integer.parseInt(gallonsTextField.text.value),
        street = streetTextField.text.value,
        city = cityTextField.text.value,
        state = stateTextField.text.value,
        zip = Integer.parseInt(zipTextField.text.value)
      )
    else null

  initOwner(App.stage)
  title = context.title
  headerText = context.savePool