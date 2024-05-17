package poolmate.dialog

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

import poolmate.{App, Context, Model}

class RepairChartDialog(context: Context, model: Model) extends Dialog[Unit]:
  val repairs = model.repairList

  val dateFormatter = DateTimeFormatter.ofPattern("yy.D")
  val minDate = repairs.map(a => a.on).min.format(dateFormatter).toDouble
  val maxDate = repairs.map(a => a.on).max.format(dateFormatter).toDouble

  val xAxis = NumberAxis(axisLabel = s"${context.repairChartYearDay} [$minDate - $maxDate]", lowerBound = minDate, upperBound = maxDate, tickUnit = 1)
  
  val yAxis = NumberAxis(axisLabel = s"${context.repairChartCosts}", lowerBound = 0.0, upperBound = 1000.00, tickUnit = 100.00)
  
  val chart = LineChart[Number, Number](xAxis, yAxis)
  chart.padding = Insets(6)

  val minCost = repairs.map(r => r.cost).min.toInt
  val maxCost = repairs.map(r => r.cost).max.toInt
  val avgCost = (repairs.map(r => r.cost).sum / repairs.length).toInt

  val series = new XYChart.Series[Number, Number]:
    name = s"${context.min} $minCost  ${context.max} $maxCost  ${context.avg} $avgCost"

  repairs foreach { repair => 
    series.data() += XYChart.Data[Number, Number](repair.on.format(dateFormatter).toDouble, repair.cost) 
  }
  chart.data = series

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox:
    spacing = 6; children = List(chart)

  initOwner(App.stage)
  title = context.repairChart
  headerText = context.repairCosts