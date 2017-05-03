package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import jfxtras.scene.control.LocalTimePicker
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Lifecycle
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class LifecycleDialog(conf: Config, lifecycle: Lifecycle) extends Dialog[Lifecycle]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val createdDatePicker = new DatePicker { value = lifecycle.created }
  val activeCheckBox = new CheckBox { selected = lifecycle.active }
  val pumpOnTimePicker = new LocalTimePicker { localTimeProperty.value = lifecycle.pumpOn }
  val pumpOffTimePicker = new LocalTimePicker { localTimeProperty.value = lifecycle.pumpOff }
  val controls = List[(String, Region)](
    conf.getString("lifecycle-created") -> createdDatePicker,
    conf.getString("lifecycle-active") -> activeCheckBox,
    conf.getString("lifecycle-pump-on") -> pumpOnTimePicker,
    conf.getString("lifecycle-pump-ff") -> pumpOffTimePicker
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-lifecycle")

  val saveButton = dialog.lookupButton(saveButtonType)
  activeCheckBox.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      lifecycle.copy(created = createdDatePicker.value.value,
                     active = activeCheckBox.selected.value,
                     pumpOn = pumpOnTimePicker.localTimeProperty.value,
                     pumpOff = pumpOffTimePicker.localTimeProperty.value)
    else null
  }
}