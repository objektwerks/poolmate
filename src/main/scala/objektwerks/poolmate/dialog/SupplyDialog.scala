package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Supply
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class SupplyDialog(conf: Config, supply: Supply) extends Dialog[Supply]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val purchasedDatePicker = new DatePicker { value = supply.purchased }
  val itemTextField = new TextField { text = supply.item }
  val unitTextField = new TextField { text = supply.unit }
  val amountTextField = new TextField { text = supply.amount.toString }
  val costTextField = new TextField { text = supply.cost.toString }
  val controls = List[(String, Region)](
    conf.getString("supply-purchased") -> purchasedDatePicker,
    conf.getString("supply-item") -> itemTextField,
    conf.getString("supply-unit") -> unitTextField,
    conf.getString("supply-amount") -> amountTextField,
    conf.getString("supply-cost") -> costTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-supply")

  val saveButton = dialog.lookupButton(saveButtonType)
  val isNotDouble = (text: String) => !text.matches("[0-9]{1,13}(\\.[0-9]+)?")
  amountTextField.text.onChange { (_, oldValue, newValue) => if (isNotDouble(newValue)) amountTextField.text.value = oldValue }
  costTextField.text.onChange { (_, oldValue, newValue) => if (isNotDouble(newValue)) costTextField.text.value = oldValue }
  itemTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  unitTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      supply.copy(purchased = purchasedDatePicker.value.value,
                  item = itemTextField.text.value,
                  unit = unitTextField.text.value,
                  amount = amountTextField.text.value.toDouble,
                  cost = costTextField.text.value.toDouble)
    else null
  }
}