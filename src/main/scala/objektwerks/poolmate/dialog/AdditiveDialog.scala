package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Additive
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class AdditiveDialog(conf: Config, additive: Additive) extends Dialog[Additive]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val onDatePicker = new DatePicker { value = additive.on }
  val chemicalTextField = new TextField { text = additive.chemical }
  val unitTextField = new TextField { text = additive.unit }
  val amountTextField = new TextField { text = additive.amount.toString }
  val controls = List[(String, Region)](
    conf.getString("additive-on") -> onDatePicker,
    conf.getString("additive-chemical") -> chemicalTextField,
    conf.getString("additive-unit") -> unitTextField,
    conf.getString("additive-amount") -> amountTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-additive")

  val saveButton = dialog.lookupButton(saveButtonType)
  val isNotDouble = (text: String) => !text.matches("[0-9]{1,13}(\\.[0-9]+)?")
  amountTextField.text.onChange { (_, oldValue, newValue) => if (isNotDouble(newValue)) amountTextField.text.value = oldValue }
  chemicalTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  unitTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      additive.copy(on = onDatePicker.value.value,
                    chemical = chemicalTextField.text.value,
                    unit = unitTextField.text.value,
                    amount = amountTextField.text.value.toDouble)
    else null
  }
}