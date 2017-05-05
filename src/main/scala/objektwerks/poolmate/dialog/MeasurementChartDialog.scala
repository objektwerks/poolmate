package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.model.Model

import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Dialog

class MeasurementChartDialog(conf: Config, courses: ObservableBuffer[Measurement], model: Model) extends Dialog[Unit] {

}