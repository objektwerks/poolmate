package poolmate.dialog

import com.typesafe.config.Config

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, CheckBox, Dialog, DatePicker}
import scalafx.scene.layout.Region

import poolmate.{App, Cleaning, Entity}
import poolmate.pane.ControlGridPane

class CleaningDialog(conf: Config, cleaning: Cleaning) extends Dialog[Cleaning]:
  val onDatePicker = new DatePicker:
    value = Entity.toLocalDate(cleaning.on)

  val deckCheckBox = new CheckBox:
    selected = cleaning.deck

  val brushCheckBox = new CheckBox:
    selected = cleaning.brush

  val netCheckBox = new CheckBox:
    selected = cleaning.net

  val vacuumCheckBox = new CheckBox:
    selected = cleaning.vacuum

  val skimmerBasketCheckBox = new CheckBox:
    selected = cleaning.skimmerBasket

  val pumpBasketCheckBox = new CheckBox:
    selected = cleaning.pumpBasket

  val pumpFilterCheckBox = new CheckBox:
    selected = cleaning.pumpFilter

  val controls = List[(String, Region)](
    conf.getString("cleaning-on") -> onDatePicker,
    conf.getString("cleaning-deck") -> deckCheckBox,
    conf.getString("cleaning-brush") -> brushCheckBox,
    conf.getString("cleaning-net") -> netCheckBox,
    conf.getString("cleaning-vacuum") -> vacuumCheckBox,
    conf.getString("cleaning-skimmer-basket") -> skimmerBasketCheckBox,
    conf.getString("cleaning-pump-basket") -> pumpBasketCheckBox,
    conf.getString("cleaning-pump-filter") -> pumpFilterCheckBox
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(conf.getString("save"), ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  // Not used! Why? val saveButton = dialog.lookupButton(saveButtonType)

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      cleaning.copy(on = onDatePicker.value.value.toString,
        deck = deckCheckBox.selected.value,
        brush = deckCheckBox.selected.value,
        net = netCheckBox.selected.value,
        vacuum = vacuumCheckBox.selected.value,
        skimmerBasket = skimmerBasketCheckBox.selected.value,
        pumpBasket = pumpBasketCheckBox.selected.value,
        pumpFilter = pumpFilterCheckBox.selected.value)
    else null

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-cleaning") 