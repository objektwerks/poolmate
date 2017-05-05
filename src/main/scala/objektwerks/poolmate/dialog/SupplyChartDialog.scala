package objektwerks.poolmate.dialog

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

class SupplyChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val supplies = model.supplyList
  val xAxis = CategoryAxis(supplies.map(s => s.item).distinct)
  xAxis.label = conf.getString("supply-chart-supplies")
  val yAxis = NumberAxis(axisLabel = conf.getString("supply-chart-costs"), lowerBound = 0, upperBound = 1000.00, tickUnit = 100.00)
  val chart = BarChart[String, Number](xAxis, yAxis)
  chart.categoryGap = 25.0

  val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
  supplies foreach { supply =>
    val series = new XYChart.Series[String, Number] {
      val year = supply.purchased.format(dateFormatter)
      name = year
      data() += XYChart.Data[String, Number](year, supply.cost)
    }
    chart.data() += series
  }

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox { spacing = 6; children = List(chart) }

  initOwner(App.stage)
  title = conf.getString("supply-chart")
  headerText = conf.getString("supply-costs")
}