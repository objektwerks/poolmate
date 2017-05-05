package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.entity.Additive
import objektwerks.poolmate.model.Model

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Dialog

class AdditiveChartDialog(conf: Config, courses: ObservableBuffer[Additive], model: Model) extends Dialog[Unit] {

}