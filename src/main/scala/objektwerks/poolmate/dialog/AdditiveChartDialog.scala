package objektwerks.poolmate.dialog

import java.time.format.DateTimeFormatter

import com.typesafe.config.Config
import objektwerks.poolmate.App
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

class AdditiveChartDialog(conf: Config, model: Model) extends Dialog[Unit] {
  val additives = model.additiveList
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
  val xAxis = CategoryAxis(additives.map(a => a.on.format(dateFormatter)).distinct)
  xAxis.label = conf.getString("additive-chart-additives")
  val yAxis = NumberAxis(axisLabel = conf.getString("additive-chart-amounts"), lowerBound = 0.0, upperBound = 10.00, tickUnit = 1.00)
  val chart = BarChart[String, Number](xAxis, yAxis)
  chart.categoryGap = 25.0

  additives foreach { additive =>
    val series = new XYChart.Series[String, Number] {
      data() += XYChart.Data[String, Number](additive.chemical, additive.amount)
    }
    chart.data() += series
  }
  chart.padding = Insets(6)

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox { spacing = 6; children = List(chart) }

  initOwner(App.stage)
  title = conf.getString("additive-chart")
  headerText = conf.getString("additive-amounts")
}