package poolmate.dialog

import com.typesafe.config.Config

import poolmate.{App, Surface}
import poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class SurfaceDialog(conf: Config, surface: Surface) extends Dialog[Surface] {
  val installedDatePicker = new DatePicker {
    value = surface.installed
  }
  val kindTextField = new TextField {
    text = surface.kind
  }
  val controls = List[(String, Region)](
    conf.getString("surface-installed") -> installedDatePicker,
    conf.getString("surface-kind") -> kindTextField
  )
  val controlGridPane = new ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  kindTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      surface.copy(installed = installedDatePicker.value.value, kind = kindTextField.text.value)
    else null
  }

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-surface")
}