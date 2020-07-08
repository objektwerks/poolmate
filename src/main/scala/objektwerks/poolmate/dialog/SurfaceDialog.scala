package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Surface
import objektwerks.poolmate.pane.ControlGridPane
import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class SurfaceDialog(conf: Config, surface: Surface) extends Dialog[Surface]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val installedDatePicker = new DatePicker { value = surface.installed }
  val kindTextField = new TextField { text = surface.kind }
  val controls = List[(String, Region)](
    conf.getString("surface-installed") -> installedDatePicker,
    conf.getString("surface-kind") -> kindTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-surface")

  val saveButton = dialog.lookupButton(saveButtonType)
  kindTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      surface.copy(installed = installedDatePicker.value.value, kind = kindTextField.text.value)
    else null
  }
}