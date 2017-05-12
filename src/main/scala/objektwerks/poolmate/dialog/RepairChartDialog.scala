package objektwerks.poolmate.dialog

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

class RepairChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val repairs = model.repairList
  val dateFormatter = DateTimeFormatter.ofPattern("yy.D")
  val dates = repairs.map(a => a.on.format(dateFormatter).toDouble)
  val minDate = dates.min
  val maxDate = dates.max

  val xAxis = NumberAxis(axisLabel = s"${conf.getString("repair-chart-year-day")} [$minDate - $maxDate]", lowerBound = minDate, upperBound = maxDate, tickUnit = 1)
  val yAxis = NumberAxis(axisLabel = s"${conf.getString("repair-chart-costs")}", lowerBound = 0.0, upperBound = 1000.00, tickUnit = 100.00)
  val chart = LineChart[Number, Number](xAxis, yAxis)
  chart.padding = Insets(6)

  val series = new XYChart.Series[Number, Number] { name = conf.getString("repairs") }
  repairs foreach { repair => series.data() += XYChart.Data[Number, Number]( repair.on.format(dateFormatter).toDouble, repair.cost ) }
  chart.data = series

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox { spacing = 6; children = List(chart) }

  initOwner(App.stage)
  title = conf.getString("repair-chart")
  headerText = conf.getString("repair-costs")
}