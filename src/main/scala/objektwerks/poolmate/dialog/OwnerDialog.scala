package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Owner
import objektwerks.poolmate.pane.ControlGridPane

import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, DatePicker, Dialog, TextField}
import scalafx.scene.layout.Region

class OwnerDialog(conf: Config, owner: Owner) extends Dialog[Owner]()  {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val sinceDatePicker = new DatePicker { value = owner.since }
  val firstTextField = new TextField { text = owner.first }
  val lastTextField = new TextField { text = owner.last }
  val emailTextField = new TextField { text = owner.email }
  val controls = List[(String, Region)](
    conf.getString("owner-since") -> sinceDatePicker,
    conf.getString("owner-first") -> firstTextField,
    conf.getString("owner-last") -> lastTextField,
    conf.getString("owner-email") -> emailTextField
  )
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-owner")

  val saveButton = dialog.lookupButton(saveButtonType)
  firstTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  lastTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }
  emailTextField.text.onChange { (_, _, newValue) => saveButton.disable = newValue.trim.isEmpty }

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      owner.copy(since = sinceDatePicker.value.value,
                 first = firstTextField.text.value,
                 last = lastTextField.text.value,
                 email = emailTextField.text.value)
    else null
  }
}