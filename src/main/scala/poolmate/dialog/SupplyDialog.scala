package poolmate.dialog

import com.typesafe.config.Config

import poolmate.{App, Resources, Supply}
import poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class SupplyDialog(conf: Config, supply: Supply) extends Dialog[Supply] {
  import Dialogs._

  val purchasedDatePicker = new DatePicker {
    value = supply.purchased
  }
  val itemTextField = new TextField {
    text = supply.item
  }
  val unitComboBox = new ComboBox[String] {
    items = Resources.units
    selectionModel().select(supply.unit)
  }
  val amountTextField = new TextField {
    text = supply.amount.toString
  }
  val costTextField = new TextField {
    text = supply.cost.toString
  }
  val controls = List[(String, Region)](
    conf.getString("supply-purchased") -> purchasedDatePicker,
    conf.getString("supply-item") -> itemTextField,
    conf.getString("supply-unit") -> unitComboBox,
    conf.getString("supply-amount") -> amountTextField,
    conf.getString("supply-cost") -> costTextField
  )
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  amountTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) amountTextField.text.value = oldValue }
  costTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) costTextField.text.value = oldValue }
  itemTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      supply.copy(purchased = purchasedDatePicker.value.value,
        item = itemTextField.text.value,
        unit = unitComboBox.selectionModel().getSelectedItem,
        amount = amountTextField.text.value.toDouble,
        cost = costTextField.text.value.toDouble)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-supply") 
}