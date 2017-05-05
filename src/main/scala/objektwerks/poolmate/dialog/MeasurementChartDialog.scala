package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.model.Model

import scalafx.scene.control.Dialog

class MeasurementChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val measurements = model.measurementList
  conf.getString("measurement-chart")

  /*
  measurement-chart = "Measurement"
  measurement-chart-measurements = "Measurements"
  measurement-chart-months = "Months"
  measurement-charts = "Measurements Charts"
   */
}