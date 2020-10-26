package objektwerks.poolmate.dialog

import java.text.DecimalFormat
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
  val dateFormatter = DateTimeFormatter.ofPattern("yy.D")
  val minDate = measurements.map(m => m.on).min.format(dateFormatter).toDouble
  val maxDate = measurements.map(m => m.on).max.format(dateFormatter).toDouble

  import MeasurementCharts._

  val tempLineChart = buildTempLineChart(conf, measurements, minDate, maxDate)
  val tempTab = new Tab {
    text = conf.getString("measurement-chart-temp"); closable = false; content = tempLineChart
  }

  val hardnessLineChart = buildHardnessLineChart(conf, measurements, minDate, maxDate)
  val hardnessTab = new Tab {
    text = conf.getString("measurement-chart-hardness"); closable = false; content = hardnessLineChart
  }

  val totalChlorineLineChart = buildTotalChlorineLineChart(conf, measurements, minDate, maxDate)
  val totalChlorineTab = new Tab {
    text = conf.getString("measurement-chart-total-chlorine"); closable = false; content = totalChlorineLineChart
  }

  val bromineLineChart = buildBromineLineChart(conf, measurements, minDate, maxDate)
  val bromineTab = new Tab {
    text = conf.getString("measurement-chart-bromine"); closable = false; content = bromineLineChart
  }

  val freeChlorineLineChart = buildFreeChlorineLineChart(conf, measurements, minDate, maxDate)
  val freeChlorineTab = new Tab {
    text = conf.getString("measurement-chart-free-chlorine"); closable = false; content = freeChlorineLineChart
  }

  val phLineChart = buildPhLineChart(conf, measurements, minDate, maxDate)
  val phTab = new Tab {
    text = conf.getString("measurement-chart-ph"); closable = false; content = phLineChart
  }

  val alkalinityLineChart = buildAlkalinityLineChart(conf, measurements, minDate, maxDate)
  val alkalinityTab = new Tab {
    text = conf.getString("measurement-chart-alkalinity"); closable = false; content = alkalinityLineChart
  }

  val cyanuricAcidLineChart = buildCyanuricAcidLineChart(conf, measurements, minDate, maxDate)
  val cyanuricAcidTab = new Tab {
    text = conf.getString("measurement-chart-cyanuric-acid"); closable = false; content = cyanuricAcidLineChart
  }

  val chartsTabPane = new TabPane {
    padding = Insets(6); tabs = Seq(tempTab, hardnessTab, totalChlorineTab, bromineTab, freeChlorineTab, phTab, alkalinityTab, cyanuricAcidTab)
  }

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox {
    spacing = 6; padding = Insets(6); children = List(chartsTabPane)
  }

  initOwner(App.stage)
  title = conf.getString("measurement-chart")
  headerText = conf.getString("measurement-charts")
}

object MeasurementCharts {
  val dateFormatter = DateTimeFormatter.ofPattern("yy.D")
  val doubleFormatter = new DecimalFormat("#.00")

  def buildTempLineChart(conf: Config,
                         measurements: ObservableBuffer[Measurement], 
                         minDate: Double, 
                         maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-temp"), yUpperBound = 100, yTickUnit = 10)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.temp) }
    chart.data = series
    val min = measurements.map(r => r.temp).min
    val max = measurements.map(r => r.temp).max
    val avg = measurements.map(r => r.temp).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildHardnessLineChart(conf: Config, 
                             measurements: ObservableBuffer[Measurement], 
                             minDate: Double, 
                             maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-hardness"), yUpperBound = 1000, yTickUnit = 100)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.hardness) }
    chart.data = series
    val min = measurements.map(r => r.hardness).min
    val max = measurements.map(r => r.hardness).max
    val avg = measurements.map(r => r.hardness).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildTotalChlorineLineChart(conf: Config, 
                                  measurements: ObservableBuffer[Measurement], 
                                  minDate: Double, 
                                  maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-total-chlorine"), yUpperBound = 10, yTickUnit = 1)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.totalChlorine) }
    chart.data = series
    val min = measurements.map(r => r.totalChlorine).min
    val max = measurements.map(r => r.totalChlorine).max
    val avg = measurements.map(r => r.totalChlorine).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildBromineLineChart(conf: Config, 
                            measurements: ObservableBuffer[Measurement], 
                            minDate: Double, 
                            maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-bromine"), yUpperBound = 20, yTickUnit = 1)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.bromine) }
    chart.data = series
    val min = measurements.map(r => r.bromine).min
    val max = measurements.map(r => r.bromine).max
    val avg = measurements.map(r => r.bromine).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildFreeChlorineLineChart(conf: Config, 
                                 measurements: ObservableBuffer[Measurement], 
                                 minDate: Double, 
                                 maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-free-chlorine"), yUpperBound = 10, yTickUnit = 1)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.freeChlorine) }
    chart.data = series
    val min = measurements.map(r => r.freeChlorine).min
    val max = measurements.map(r => r.freeChlorine).max
    val avg = measurements.map(r => r.freeChlorine).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildPhLineChart(conf: Config, 
                       measurements: ObservableBuffer[Measurement], 
                       minDate: Double, 
                       maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-ph"), yLowerBound = 6.2, yUpperBound = 8.4, yTickUnit = 0.2)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.pH) }
    chart.data = series
    val min = doubleFormatter.format(measurements.map(r => r.pH).min)
    val max = doubleFormatter.format(measurements.map(r => r.pH).max)
    val avg = doubleFormatter.format(measurements.map(r => r.pH).sum / measurements.length)
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildAlkalinityLineChart(conf: Config, 
                               measurements: ObservableBuffer[Measurement], 
                               minDate: Double, 
                               maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-alkalinity"), yUpperBound = 240, yTickUnit = 20)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.alkalinity) }
    chart.data = series
    val min = measurements.map(r => r.alkalinity).min
    val max = measurements.map(r => r.alkalinity).max
    val avg = measurements.map(r => r.alkalinity).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }

  def buildLineChart(conf: Config, 
                     minDate: Double, 
                     maxDate: Double, 
                     yLabel: String, 
                     yLowerBound: Double = 0, 
                     yUpperBound: Double, 
                     yTickUnit: Double):
  (LineChart[Number, Number], XYChart.Series[Number, Number]) = {
    val xAxis = NumberAxis(axisLabel = s"${conf.getString("measurement-chart-year-day")} [$minDate - $maxDate]", lowerBound = minDate, upperBound = maxDate, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = yLabel, lowerBound = yLowerBound, upperBound = yUpperBound, tickUnit = yTickUnit)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]()
    chart.padding = Insets(6)
    (chart, series)
  }

  def buildCyanuricAcidLineChart(conf: Config, 
                                 measurements: ObservableBuffer[Measurement], 
                                 minDate: Double, 
                                 maxDate: Double): LineChart[Number, Number] = {
    val (chart, series) = buildLineChart(conf, minDate, maxDate, yLabel = conf.getString("measurement-chart-cyanuric-acid"), yUpperBound = 300, yTickUnit = 25)
    measurements foreach { measurement => series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.cyanuricAcid) }
    chart.data = series
    val min = measurements.map(r => r.cyanuricAcid).min
    val max = measurements.map(r => r.cyanuricAcid).max
    val avg = measurements.map(r => r.cyanuricAcid).sum / measurements.length
    series.name = s"${conf.getString("min")} $min  ${conf.getString("max")} $max  ${conf.getString("avg")} $avg"
    chart
  }
}