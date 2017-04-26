package objektwerks.poolmate.pane

import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{GridPane, Region}

class ControlGridPane(controls: List[(String, Region)]) extends GridPane {
  hgap = 6
  vgap = 6
  padding = Insets(top = 6, right = 6, bottom = 6, left = 6)
  var row = 0
  for((label, control) <- controls) {
    add(new Label { text = label }, columnIndex = 0, rowIndex = row)
    add(control, columnIndex = 1, rowIndex = row)
    row = row + 1
  }
}