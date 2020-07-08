package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Timer
import objektwerks.poolmate.pane.ControlGridPane
import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class TimerDialog(conf: Config, timer: Timer) extends Dialog[Timer]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val installedDatePicker = new DatePicker { value = timer.installed }
  val modelTextField = new TextField { text = timer.model }
  val controls = List[(String, Region)](
    conf.getString("timer-installed") -> installedDatePicker,
    conf.getString("timer-model") -> modelTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-timer")

  val saveButton = dialog.lookupButton(saveButtonType)
  modelTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      timer.copy(installed = installedDatePicker.value.value, model = modelTextField.text.value)
    else null
  }
}