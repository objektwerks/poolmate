package objektwerks.poolmate.dialog

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.entity.Measurement
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog, Tab, TabPane}
import scalafx.scene.layout.VBox

class MeasurementChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val measurements = model.measurementList
  val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
  val years = measurements.map(a => a.on.format(yearFormatter).toInt)
  val minYear = years.min
  val maxYear = years.max

  import MeasurementCharts._
  val tempLineChart = buildTempLineChart(conf, measurements, minYear, maxYear)
  val tempTab = new Tab { text = conf.getString("measurement-header-temp"); closable = false; content = tempLineChart }

  val hardnessLineChart = buildHardnessLineChart(conf, measurements, minYear, maxYear)
  val hardnessTab = new Tab { text = conf.getString("measurement-header-hardness"); closable = false; content = hardnessLineChart }

  val totalChlorineLineChart = buildTotalChlorineLineChart(conf, measurements, minYear, maxYear)
  val totalChlorineTab = new Tab { text = conf.getString("measurement-header-total-chlorine"); closable = false; content = totalChlorineLineChart }

  val bromineLineChart = buildBromineLineChart(conf, measurements, minYear, maxYear)
  val bromineTab = new Tab { text = conf.getString("measurement-header-bromine"); closable = false; content = bromineLineChart }

  val freeChlorineLineChart = buildFreeChlorineLineChart(conf, measurements, minYear, maxYear)
  val freeChlorineTab = new Tab { text = conf.getString("measurement-header-free-chlorine"); closable = false; content = freeChlorineLineChart }

  val phLineChart = buildPhLineChart(conf, measurements, minYear, maxYear)
  val phTab = new Tab { text = conf.getString("measurement-header-ph"); closable = false; content = phLineChart }

  val alkalinityLineChart = buildAlkalinityLineChart(conf, measurements, minYear, maxYear)
  val alkalinityTab = new Tab { text = conf.getString("measurement-header-alkalinity"); closable = false; content = alkalinityLineChart }

  val cyanuricAcidLineChart = buildCyanuricAcidLineChart(conf, measurements, minYear, maxYear)
  val cyanuricAcidTab = new Tab { text = conf.getString("measurement-header-cyanuric-acid"); closable = false; content = cyanuricAcidLineChart }

  val chartsTabPane = new TabPane { padding = Insets(6); tabs = ObservableBuffer(tempTab, hardnessTab, totalChlorineTab, bromineTab, freeChlorineTab, phTab, alkalinityTab, cyanuricAcidTab) }

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox { spacing = 6; padding = Insets(6); children = List(chartsTabPane) }

  initOwner(App.stage)
  title = conf.getString("measurement-chart")
  headerText = conf.getString("measurement-charts")
}

object MeasurementCharts {
  val monthFormatter = DateTimeFormatter.ofPattern("MM")

  def buildLineChart(conf: Config, minYear: Int, maxYear: Int, yLabel: String, yLowerBound: Double = 0, yUpperBound: Double, yTickUnit: Double):
                    (LineChart[Number, Number], XYChart.Series[Number, Number]) = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = yLabel, lowerBound = yLowerBound, upperBound = yUpperBound, tickUnit = yTickUnit)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    (chart, series)
  }

  def buildTempLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-temp"), yUpperBound = 100, yTickUnit = 10)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.temp ) }
    chart.data = series
    chart
  }

  def buildHardnessLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-hardness"), yUpperBound = 1000, yTickUnit = 100)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.hardness ) }
    chart.data = series
    chart
  }

  def buildTotalChlorineLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-total-chlorine"), yUpperBound = 10, yTickUnit = 1)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.totalChlorine ) }
    chart.data = series
    chart
  }

  def buildBromineLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-bromine"), yUpperBound = 20, yTickUnit = 1)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.bromine ) }
    chart.data = series
    chart
  }

  def buildFreeChlorineLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-free-chlorine"), yUpperBound = 10, yTickUnit = 1)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.freeChlorine ) }
    chart.data = series
    chart
  }

  def buildPhLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-ph"), yLowerBound = 6.2, yUpperBound = 8.4, yTickUnit = 0.2)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.pH ) }
    chart.data = series
    chart
  }

  def buildAlkalinityLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-alkalinity"), yUpperBound = 240, yTickUnit = 20)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.alkalinity ) }
    chart.data = series
    chart
  }

  def buildCyanuricAcidLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minYear, maxYear, yLabel = conf.getString("measurement-header-cyanuric-acid"), yUpperBound = 300, yTickUnit = 25)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.cyanuricAcid ) }
    chart.data = series
    chart
  }
}