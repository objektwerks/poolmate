package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import Dialogs.*
import poolmate.{App, Context, Entity, Repair}
import poolmate.pane.ControlGridPane

class RepairDialog(context: Context, repair: Repair) extends Dialog[Repair]:
  val onDatePicker = new DatePicker:
    value = Entity.toLocalDate(repair.on)

  val itemTextField = new TextField:
    text = repair.item

  val costTextField = new TextField:
    text = repair.cost.toString

  val controls = List[(String, Region)](
    context.repairOn -> onDatePicker,
    context.repairItem -> itemTextField,
    context.repairCost -> costTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  costTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) then costTextField.text.value = oldValue }
  itemTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      repair.copy(on = onDatePicker.value.value.toString,
        item = itemTextField.text.value,
        cost = costTextField.text.value.toDouble)
    else null

  initOwner(App.stage)
  title = context.title
  headerText = context.saveRepair 