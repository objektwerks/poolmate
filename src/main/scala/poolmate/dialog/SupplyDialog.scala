package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, ComboBox, Dialog, DatePicker, TextField}
import scalafx.scene.layout.Region

import Dialogs.*
import poolmate.{App, Context, Entity, Supply}
import poolmate.pane.ControlGridPane

class SupplyDialog(context: Context, supply: Supply) extends Dialog[Supply]:
  val purchasedDatePicker = new DatePicker:
    value = Entity.toLocalDate(supply.purchased)

  val itemTextField = new TextField:
    text = supply.item

  val unitComboBox = new ComboBox[String]:
    items = context.units
    selectionModel().select(supply.unit)

  val amountTextField = new TextField:
    text = supply.amount.toString

  val costTextField = new TextField:
    text = supply.cost.toString

  val controls = List[(String, Region)](
    context.supplyPurchased -> purchasedDatePicker,
    context.supplyItem -> itemTextField,
    context.supplyUnit -> unitComboBox,
    context.supplyAmount -> amountTextField,
    context.supplyCost -> costTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  amountTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) then amountTextField.text.value = oldValue }
  costTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) then costTextField.text.value = oldValue }
  itemTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      supply.copy(
        purchased = purchasedDatePicker.value.value.toString,
        item = itemTextField.text.value,
        unit = unitComboBox.selectionModel().getSelectedItem,
        amount = amountTextField.text.value.toDouble,
        cost = costTextField.text.value.toDouble
      )
    else null

  initOwner(App.stage)
  title = context.title
  headerText = context.saveSupply 