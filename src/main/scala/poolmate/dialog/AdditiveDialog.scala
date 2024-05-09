package poolmate.dialog

import com.typesafe.config.Config

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, ComboBox, Dialog, DatePicker, TextField}
import scalafx.scene.layout.Region

import Dialogs.*
import poolmate.{Additive, App, Entity, Resources}
import poolmate.pane.ControlGridPane

class AdditiveDialog(conf: Config, additive: Additive) extends Dialog[Additive]:
  val onDatePicker = new DatePicker:
    value = Entity.toLocalDate(additive.on)

  val chemicalTextField = new TextField:
    text = additive.chemical

  val unitComboBox = new ComboBox[String]:
    items = Resources.units
    selectionModel().select(additive.unit)

  val amountTextField = new TextField:
    text = additive.amount.toString

  val controls = List[(String, Region)](
    conf.getString("additive-on") -> onDatePicker,
    conf.getString("additive-chemical") -> chemicalTextField,
    conf.getString("additive-unit") -> unitComboBox,
    conf.getString("additive-amount") -> amountTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  amountTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) then amountTextField.text.value = oldValue }

  val saveButton = dialog.lookupButton(saveButtonType)
  chemicalTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      additive.copy(on = onDatePicker.value.value.toString,
        chemical = chemicalTextField.text.value,
        unit = unitComboBox.selectionModel().getSelectedItem,
        amount = amountTextField.text.value.toDouble)
    else null

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-additive")