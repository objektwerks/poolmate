package objektwerks.poolmate.dialog

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

class MeasurementChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val measurements = model.measurementList
  val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
  val years = measurements.map(a => a.on.format(yearFormatter).toInt)
  val minYear = years.min
  val maxYear = years.max

  import MeasurementCharts._
  val tempLineChart = buildTempLineChart(conf, measurements, minYear, maxYear)


  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox { spacing = 6; children = List(tempLineChart) }

  initOwner(App.stage)
  title = conf.getString("measurement-chart")
  headerText = conf.getString("measurements-charts")
}

/*
measurement-chart-temp = "Temp"
measurement-chart-hardness = "Hardness"
measurement-chart-total-chlorine = "Total\nChlorine"
measurement-chart-bromine = "Bromine"
measurement-chart-free-chlorine = "Free\nChlorine"
measurement-chart-ph = "pH"
measurement-chart-alkalinity = "Alkalinity"
measurement-chart-cyanuric-acid = "Cyanuric\nAcid"
 */

object MeasurementCharts {
  def buildTempLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-chart-temp"), lowerBound = 0, upperBound = 100.00, tickUnit = 10.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.temp )
    }
    chart.data = series
    chart
  }
}