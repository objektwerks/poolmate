package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Cleaning
import objektwerks.poolmate.pane.ControlGridPane
import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.Region

class CleaningDialog(conf: Config, cleaning: Cleaning) extends Dialog[Cleaning]() {
  val saveButtonType = new ButtonType(conf.getString("save"), ButtonData.OKDone)
  val onDatePicker = new DatePicker {
    value = cleaning.on
  }
  val deckCheckBox = new CheckBox {
    selected = cleaning.deck
  }
  val brushCheckBox = new CheckBox {
    selected = cleaning.brush
  }
  val netCheckBox = new CheckBox {
    selected = cleaning.net
  }
  val vacuumCheckBox = new CheckBox {
    selected = cleaning.vacuum
  }
  val skimmerBasketCheckBox = new CheckBox {
    selected = cleaning.skimmerBasket
  }
  val pumpBasketCheckBox = new CheckBox {
    selected = cleaning.pumpBasket
  }
  val pumpFilterCheckBox = new CheckBox {
    selected = cleaning.pumpFilter
  }

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
  val controlGridPane = new ControlGridPane(controls)
  val dialog = dialogPane()
  dialog.buttonTypes = List(saveButtonType, ButtonType.Cancel)
  dialog.content = controlGridPane

  initOwner(App.stage)
  title = conf.getString("title")
  headerText = conf.getString("save-cleaning")

  val saveButton = dialog.lookupButton(saveButtonType)

  resultConverter = dialogButton => {
    if (dialogButton == saveButtonType)
      cleaning.copy(on = onDatePicker.value.value,
        deck = deckCheckBox.selected.value,
        brush = deckCheckBox.selected.value,
        net = netCheckBox.selected.value,
        vacuum = vacuumCheckBox.selected.value,
        skimmerBasket = skimmerBasketCheckBox.selected.value,
        pumpBasket = pumpBasketCheckBox.selected.value,
        pumpFilter = pumpFilterCheckBox.selected.value)
    else null
  }
}