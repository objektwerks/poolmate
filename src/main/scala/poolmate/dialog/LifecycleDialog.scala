package poolmate.dialog

import com.typesafe.config.Config

import jfxtras.scene.control.LocalTimePicker

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, CheckBox, Dialog, DatePicker}
import scalafx.scene.layout.Region

import poolmate.{App, Entity, Lifecycle}
import poolmate.pane.ControlGridPane

class LifecycleDialog(conf: Config, lifecycle: Lifecycle) extends Dialog[Lifecycle]:
  val createdDatePicker = new DatePicker:
    value = Entity.toLocalDate(lifecycle.created)

  val activeCheckBox = new CheckBox:
    selected = lifecycle.active

  val pumpOnTimePicker = new LocalTimePicker:
    localTimeProperty.value = Entity.toLocalTime(lifecycle.pumpOn)

  val pumpOffTimePicker = new LocalTimePicker:
    localTimeProperty.value = Entity.toLocalTime(lifecycle.pumpOff)

  val controls = List[(String, Region)](
    conf.getString("lifecycle-created") -> createdDatePicker,
    conf.getString("lifecycle-active") -> activeCheckBox,
    conf.getString("lifecycle-pump-on") -> pumpOnTimePicker,
    conf.getString("lifecycle-pump-off") -> pumpOffTimePicker
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  pumpOffTimePicker.localTimeProperty.onChange { (_, oldLocalTime, newLocalTime) =>
    if (newLocalTime.getHour <= pumpOnTimePicker.localTimeProperty.get.getHour) then
      pumpOffTimePicker.localTimeProperty.value = oldLocalTime
  }

  resultConverter = dialogButton => 
    if (dialogButton == saveButtonType) then
      lifecycle.copy(created = createdDatePicker.value.value.toString,
        active = activeCheckBox.selected.value,
        pumpOn = pumpOnTimePicker.localTimeProperty.value.toString,
        pumpOff = pumpOffTimePicker.localTimeProperty.value.toString)
    else null

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-lifecycle") 