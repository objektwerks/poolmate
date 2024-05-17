package poolmate.dialog

import scalafx.Includes.*
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ButtonType, CheckBox, Dialog, DatePicker}
import scalafx.scene.layout.Region

import poolmate.{App, Cleaning, Context, Entity}
import poolmate.pane.ControlGridPane

class CleaningDialog(context: Context, cleaning: Cleaning) extends Dialog[Cleaning]:
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
    context.cleaningOn -> onDatePicker,
    context.cleaningDeck -> deckCheckBox,
    context.cleaningBrush -> brushCheckBox,
    context.cleaningNet -> netCheckBox,
    context.cleaningVacuum -> vacuumCheckBox,
    context.cleaningSkimmerBasket -> skimmerBasketCheckBox,
    context.cleaningPumpBasket -> pumpBasketCheckBox,
    context.cleaningPumpFilter -> pumpFilterCheckBox
  )
  val controlGridPane = ControlGridPane(controls)

  val dialog = dialogPane()
  val saveButtonType = ButtonType(context.save, ButtonData.OKDone)
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  // Not used! Why? val saveButton = dialog.lookupButton(saveButtonType)

  resultConverter = dialogButton =>
    if (dialogButton == saveButtonType) then
      cleaning.copy(
        on = onDatePicker.value.value.toString,
        deck = deckCheckBox.selected.value,
        brush = deckCheckBox.selected.value,
        net = netCheckBox.selected.value,
        vacuum = vacuumCheckBox.selected.value,
        skimmerBasket = skimmerBasketCheckBox.selected.value,
        pumpBasket = pumpBasketCheckBox.selected.value,
        pumpFilter = pumpFilterCheckBox.selected.value
      )
    else null

  initOwner(App.stage)
  title = context.title
  headerText = context.saveCleaning 