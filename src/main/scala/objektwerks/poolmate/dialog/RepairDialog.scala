package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Repair
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class RepairDialog(conf: Config, repair: Repair) extends Dialog[Repair]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val onDatePicker = new DatePicker { value = repair.on }
  val itemTextField = new TextField { text = repair.item }
  val costTextField = new TextField { text = repair.cost.toString }
  val controls = List[(String, Region)](
    conf.getString("repair-on") -> onDatePicker,
    conf.getString("repair-item") -> itemTextField,
    conf.getString("repair-cost") -> costTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-repair")

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
}