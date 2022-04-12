package poolmate.dialog

import com.typesafe.config.Config

import poolmate.{App, Pool}
import poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class PoolDialog(conf: Config, pool: Pool) extends Dialog[Pool] {
  val builtDatePicker = new DatePicker {
    value = pool.built
  }
  val gallonsTextField = new TextField {
    text = pool.gallons.toString
  }
  val streetTextField = new TextField {
    text = pool.street
  }
  val cityTextField = new TextField {
    text = pool.city
  }
  val stateTextField = new TextField {
    text = pool.state
  }
  val zipTextField = new TextField {
    text = pool.zip.toString
  }
  val controls = List[(String, Region)](
    conf.getString("pool-built") -> builtDatePicker,
    conf.getString("pool-gallons") -> gallonsTextField,
    conf.getString("pool-street") -> streetTextField,
    conf.getString("pool-city") -> cityTextField,
    conf.getString("pool-state") -> stateTextField,
    conf.getString("pool-zip") -> zipTextField
  )
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  gallonsTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) gallonsTextField.text.value = oldValue }
  streetTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  cityTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  stateTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  zipTextField.text.onChange { (_, oldValue, newValue) => if (isNotNumeric(newValue)) zipTextField.text.value = oldValue }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      pool.copy(built = builtDatePicker.value.value,
        gallons = Integer.parseInt(gallonsTextField.text.value),
        street = streetTextField.text.value,
        city = cityTextField.text.value,
        state = stateTextField.text.value,
        zip = Integer.parseInt(zipTextField.text.value))
    else null
  }

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-pool")
}