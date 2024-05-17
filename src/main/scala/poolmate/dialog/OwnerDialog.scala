package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import poolmate.{App, Context, Entity, Owner}
import poolmate.pane.ControlGridPane

class OwnerDialog(context: Context, owner: Owner) extends Dialog[Owner]:
  val sinceDatePicker = new DatePicker:
    value = Entity.toLocalDate(owner.since)

  val firstTextField = new TextField:
    text = owner.first

  val lastTextField = new TextField:
    text = owner.last

  val emailTextField = new TextField:
    text = owner.email

  val controls = List[(String, Region)](
    context.ownerSince -> sinceDatePicker,
    context.ownerFirst -> firstTextField,
    context.ownerLast -> lastTextField,
    context.ownerEmail -> emailTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  firstTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  lastTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  emailTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      owner.copy(since = sinceDatePicker.value.value.toString,
        first = firstTextField.text.value,
        last = lastTextField.text.value,
        email = emailTextField.text.value)
    else null

  initOwner(App.stage)
  title = context.getString("title")
  headerText = context.getString("save-owner") 