package poolmate.dialog

import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog, Tab, TabPane}
import scalafx.scene.layout.VBox

import poolmate.{App, Context, Measurement, Model}

class MeasurementChartDialog(context: Context, model: Model) extends Dialog[Unit]:
  val measurements = model.measurementList

  val dateFormatter = DateTimeFormatter.ofPattern("yy.D")
  val minDate = measurements.map(m => m.on).min.format(dateFormatter).toDouble
  val maxDate = measurements.map(m => m.on).max.format(dateFormatter).toDouble

  import MeasurementCharts.*

  val tempLineChart = buildTempLineChart(context, measurements, minDate, maxDate)
  val tempTab = new Tab:
    text = context.measurementChartTemp
    closable = false
    content = tempLineChart

  val hardnessLineChart = buildHardnessLineChart(context, measurements, minDate, maxDate)
  val hardnessTab = new Tab:
    text = context.measurementChartHardness
    closable = false
    content = hardnessLineChart

  val totalChlorineLineChart = buildTotalChlorineLineChart(context, measurements, minDate, maxDate)
  val totalChlorineTab = new Tab:
    text = context.measurementChartTotalChlorine
    closable = false
    content = totalChlorineLineChart

  val bromineLineChart = buildBromineLineChart(context, measurements, minDate, maxDate)
  val bromineTab = new Tab:
    text = context.measurementChartBromine
    closable = false
    content = bromineLineChart

  val freeChlorineLineChart = buildFreeChlorineLineChart(context, measurements, minDate, maxDate)
  val freeChlorineTab = new Tab:
    text = context.measurementChartFreeChlorine
    closable = false
    content = freeChlorineLineChart

  val phLineChart = buildPhLineChart(context, measurements, minDate, maxDate)
  val phTab = new Tab:
    text = context.measurementChartPh
    closable = false
    content = phLineChart

  val alkalinityLineChart = buildAlkalinityLineChart(context, measurements, minDate, maxDate)
  val alkalinityTab = new Tab:
    text = context.measurementChartAlkalinity
    closable = false
    content = alkalinityLineChart

  val cyanuricAcidLineChart = buildCyanuricAcidLineChart(context, measurements, minDate, maxDate)
  val cyanuricAcidTab = new Tab:
    text = context.measurementChartCyanuricAcid
    closable = false
    content = cyanuricAcidLineChart

  val chartsTabPane = new TabPane:
    padding = Insets(6)
    tabs = Seq(tempTab, hardnessTab, totalChlorineTab, bromineTab, freeChlorineTab, phTab, alkalinityTab, cyanuricAcidTab)

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox:
    spacing = 6
    padding = Insets(6)
    children = List(chartsTabPane)

  initOwner(App.stage)
  title = context.measurementChart
  headerText = context.measurementCharts

object MeasurementCharts:
  val dateFormatter = DateTimeFormatter.ofPattern("yy.D")
  val doubleFormatter = new DecimalFormat("#.00")

  def buildLineChart(context: Context, 
                     minDate: Double, 
                     maxDate: Double, 
                     yLabel: String, 
                     yLowerBound: Double = 0, 
                     yUpperBound: Double, 
                     yTickUnit: Double): (LineChart[Number, Number], XYChart.Series[Number, Number]) =
    val xAxis = NumberAxis(axisLabel = s"${context.measurementChartYearDay} [$minDate - $maxDate]", lowerBound = minDate, upperBound = maxDate, tickUnit = 1)
    val yAxis = NumberAxis(axisLabel = yLabel, lowerBound = yLowerBound, upperBound = yUpperBound, tickUnit = yTickUnit)
    val chart = LineChart[Number, Number](xAxis, yAxis)
    val series = new XYChart.Series[Number, Number]()
    chart.padding = Insets(6)
    (chart, series)

  def buildTempLineChart(context: Context,
                         measurements: ObservableBuffer[Measurement], 
                         minDate: Double, 
                         maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context,
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartTemp, 
                                         yUpperBound = 100, 
                                         yTickUnit = 10)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.temp) 
    }
    chart.data = series
    val min = measurements.map(m => m.temp).min
    val max = measurements.map(m => m.temp).max
    val avg = measurements.map(m => m.temp).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildHardnessLineChart(context: Context, 
                             measurements: ObservableBuffer[Measurement], 
                             minDate: Double, 
                             maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartHardness, 
                                         yUpperBound = 1000, 
                                         yTickUnit = 100)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.hardness) 
    }
    chart.data = series
    val min = measurements.map(m => m.hardness).min
    val max = measurements.map(m => m.hardness).max
    val avg = measurements.map(m => m.hardness).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildTotalChlorineLineChart(context: Context, 
                                  measurements: ObservableBuffer[Measurement], 
                                  minDate: Double, 
                                  maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartTotalChlorine, 
                                         yUpperBound = 10, 
                                         yTickUnit = 1)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.totalChlorine) 
    }
    chart.data = series
    val min = measurements.map(m => m.totalChlorine).min
    val max = measurements.map(m => m.totalChlorine).max
    val avg = measurements.map(m => m.totalChlorine).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildBromineLineChart(context: Context, 
                            measurements: ObservableBuffer[Measurement], 
                            minDate: Double, 
                            maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartBromine, 
                                         yUpperBound = 20, 
                                         yTickUnit = 1)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.bromine) 
    }
    chart.data = series
    val min = measurements.map(m => m.bromine).min
    val max = measurements.map(m => m.bromine).max
    val avg = measurements.map(m => m.bromine).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildFreeChlorineLineChart(context: Context, 
                                 measurements: ObservableBuffer[Measurement], 
                                 minDate: Double, 
                                 maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartFreeChlorine, 
                                         yUpperBound = 10, 
                                         yTickUnit = 1)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.freeChlorine) 
    }
    chart.data = series
    val min = measurements.map(m => m.freeChlorine).min
    val max = measurements.map(m => m.freeChlorine).max
    val avg = measurements.map(m => m.freeChlorine).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildPhLineChart(context: Context, 
                       measurements: ObservableBuffer[Measurement], 
                       minDate: Double, 
                       maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartPh, 
                                         yLowerBound = 6.2, 
                                         yUpperBound = 8.4, 
                                         yTickUnit = 0.2)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.pH) 
    }
    chart.data = series
    val min = doubleFormatter.format(measurements.map(m => m.pH).min)
    val max = doubleFormatter.format(measurements.map(m => m.pH).max)
    val avg = doubleFormatter.format(measurements.map(m => m.pH).sum / measurements.length)
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildAlkalinityLineChart(context: Context, 
                               measurements: ObservableBuffer[Measurement], 
                               minDate: Double, 
                               maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartAlkalinity, 
                                         yUpperBound = 240, 
                                         yTickUnit = 20)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.alkalinity) 
    }
    chart.data = series
    val min = measurements.map(m => m.alkalinity).min
    val max = measurements.map(m => m.alkalinity).max
    val avg = measurements.map(m => m.alkalinity).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart

  def buildCyanuricAcidLineChart(context: Context, 
                                 measurements: ObservableBuffer[Measurement], 
                                 minDate: Double, 
                                 maxDate: Double): LineChart[Number, Number] =
    val (chart, series) = buildLineChart(context, 
                                         minDate, 
                                         maxDate, 
                                         yLabel = context.measurementChartCyanuricAcid, 
                                         yUpperBound = 300, 
                                         yTickUnit = 25)
    measurements foreach { measurement => 
      series.data() += XYChart.Data[Number, Number](measurement.on.format(dateFormatter).toDouble, measurement.cyanuricAcid) 
    }
    chart.data = series
    val min = measurements.map(m => m.cyanuricAcid).min
    val max = measurements.map(m => m.cyanuricAcid).max
    val avg = measurements.map(m => m.cyanuricAcid).sum / measurements.length
    series.name = s"${context.min} $min  ${context.max} $max  ${context.avg} $avg"
    chart