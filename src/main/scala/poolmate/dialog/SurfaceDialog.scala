package poolmate.dialog

import com.typesafe.config.Config

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import poolmate.{App, Entity, Surface}
import poolmate.pane.ControlGridPane

class SurfaceDialog(conf: Config, surface: Surface) extends Dialog[Surface]:
  val installedDatePicker = new DatePicker:
    value = Entity.toLocalDate(surface.installed)

  val kindTextField = new TextField:
    text = surface.kind

  val controls = List[(String, Region)](
    conf.getString("surface-installed") -> installedDatePicker,
    conf.getString("surface-kind") -> kindTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  val saveButton = dialog.lookupButton(saveButtonType)
  kindTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      surface.copy(
        installed = installedDatePicker.value.value.toString,
        kind = kindTextField.text.value
      )
    else null

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-surface")