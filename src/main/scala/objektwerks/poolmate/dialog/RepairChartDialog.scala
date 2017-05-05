package objektwerks.poolmate.dialog

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

class RepairChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val repairs = model.repairList
  val monthFormatter = DateTimeFormatter.ofPattern("MM")
  val minMonth = repairs.map(a => a.on.format(monthFormatter).toInt).min
  val maxMonth = repairs.map(a => a.on.format(monthFormatter).toInt).max

  val xAxis = NumberAxis(axisLabel = s"${conf.getString("repair-chart-months")} [$minMonth - $maxMonth]", lowerBound = minMonth, upperBound = maxMonth + 1, tickUnit = 1)
  val yAxis = NumberAxis(axisLabel = conf.getString("repair-chart-costs"), lowerBound = 0, upperBound = 1000.00, tickUnit = 100.00)
  val chart = LineChart[Number, Number](xAxis, yAxis)

  val series = new XYChart.Series[Number, Number]{ name = conf.getString("repair-chart-cost") }
  val monthDayFormatter = DateTimeFormatter.ofPattern("MM.dd")
  repairs foreach { repair =>
    series.data() += XYChart.Data[Number, Number]( repair.on.format(monthDayFormatter).toDouble, repair.cost.toInt )
  }
  chart.data = series

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox { spacing = 6; children = List(chart) }

  initOwner(App.stage)
  title = conf.getString("repair-chart")
  headerText = conf.getString("repair-costs")
}