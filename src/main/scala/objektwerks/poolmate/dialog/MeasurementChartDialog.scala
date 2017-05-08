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
  dialog.content = new VBox { spacing = 6; children = List(chartsTabPane) }

  initOwner(App.stage)
  title = conf.getString("measurement-chart")
  headerText = conf.getString("measurements-charts")
}

object MeasurementCharts {
  def buildTempLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-temp"), lowerBound = 0, upperBound = 100.00, tickUnit = 10.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.temp )
    }
    chart.data = series
    chart
  }

  def buildHardnessLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-hardness"), lowerBound = 0, upperBound = 1000.00, tickUnit = 100.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.hardness )
    }
    chart.data = series
    chart
  }

  def buildTotalChlorineLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-total-chlorine"), lowerBound = 0, upperBound = 10.00, tickUnit = 1.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.totalChlorine )
    }
    chart.data = series
    chart
  }

  def buildBromineLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-bromine"), lowerBound = 0, upperBound = 20.00, tickUnit = 1.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.bromine )
    }
    chart.data = series
    chart
  }

  def buildFreeChlorineLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-free-chlorine"), lowerBound = 0, upperBound = 10.00, tickUnit = 1.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.freeChlorine )
    }
    chart.data = series
    chart
  }

  def buildPhLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-ph"), lowerBound = 6.2, upperBound = 8.4, tickUnit = 0.2)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.pH )
    }
    chart.data = series
    chart
  }

  def buildAlkalinityLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-alkalinity"), lowerBound = 0, upperBound = 240.00, tickUnit = 20.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.alkalinity )
    }
    chart.data = series
    chart
  }

  def buildCyanuricAcidLineChart(conf: Config, measurements: ObservableBuffer[Measurement], minYear: Int, maxYear: Int): LineChart[Number, Number] = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-years")} [$minYear - $maxYear]", lowerBound = minYear, upperBound = maxYear + 1, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = conf.getString("measurement-header-cyanuric-acid"), lowerBound = 0, upperBound = 300.00, tickUnit = 25.00)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]{ name = conf.getString("measurement-chart") }
    val monthFormatter = DateTimeFormatter.ofPattern("MM")
    measurements foreach { measurement =>
      series.data() += XYChart.Data[Number, Number]( measurement.on.format(monthFormatter).toInt, measurement.cyanuricAcid )
    }
    chart.data = series
    chart
  }
}