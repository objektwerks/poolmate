package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

import poolmate.{App, Context, Entity, Surface}
import poolmate.pane.ControlGridPane

class SurfaceDialog(context: Context, surface: Surface) extends Dialog[Surface]:
  val installedDatePicker = new DatePicker:
    value = Entity.toLocalDate(surface.installed)

  val kindTextField = new TextField:
    text = surface.kind

  val controls = List[(String, Region)](
    context.surfaceInstalled -> installedDatePicker,
    context.surfaceKind -> kindTextField
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
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
  title = context.title
  headerText = context.saveSurface