package poolmate.dialog

import com.typesafe.config.Config

import poolmate.Additive
import poolmate.pane.ControlGridPane
import poolmate.{App, Resources}

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class AdditiveDialog(conf: Config, additive: Additive) extends Dialog[Additive] {
  val onDatePicker = new DatePicker {
    value = additive.on
  }
  val chemicalTextField = new TextField {
    text = additive.chemical
  }
  val unitComboBox = new ComboBox[String] {
    items = Resources.units
    selectionModel().select(additive.unit)
  }
  val amountTextField = new TextField {
    text = additive.amount.toString
  }
  val controls = List[(String, Region)](
    conf.getString("additive-on") -> onDatePicker,
    conf.getString("additive-chemical") -> chemicalTextField,
    conf.getString("additive-unit") -> unitComboBox,
    conf.getString("additive-amount") -> amountTextField
  )
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  amountTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) amountTextField.text.value = oldValue }

  val saveButton = dialog.lookupButton(saveButtonType)
  chemicalTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      additive.copy(on = onDatePicker.value.value,
        chemical = chemicalTextField.text.value,
        unit = unitComboBox.selectionModel().getSelectedItem,
        amount = amountTextField.text.value.toDouble)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-additive") 
}