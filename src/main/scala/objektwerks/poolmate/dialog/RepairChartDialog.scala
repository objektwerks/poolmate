package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.entity.Repair
import objektwerks.poolmate.model.Model

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Dialog

class RepairChartDialog(conf: Config, courses: ObservableBuffer[Repair], model: Model) extends Dialog[Unit] {

}