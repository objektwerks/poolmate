package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.entity.Supply
import objektwerks.poolmate.model.Model

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Dialog

class SupplyChartDialog(conf: Config, courses: ObservableBuffer[Supply], model: Model) extends Dialog[Unit] {

}