package poolmate.dialog

import com.typesafe.config.Config

import poolmate.App
import poolmate.Repair
import poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class RepairDialog(conf: Config, repair: Repair) extends Dialog[Repair] {
  val onDatePicker = new DatePicker {
    value = repair.on
  }
  val itemTextField = new TextField {
    text = repair.item
  }
  val costTextField = new TextField {
    text = repair.cost.toString
  }
  val controls = List[(String, Region)](
    conf.getString("repair-on") -> onDatePicker,
    conf.getString("repair-item") -> itemTextField,
    conf.getString("repair-cost") -> costTextField
  )
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  costTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) costTextField.text.value = oldValue }
  itemTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      repair.copy(on = onDatePicker.value.value,
        item = itemTextField.text.value,
        cost = costTextField.text.value.toDouble)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-repair") 
}