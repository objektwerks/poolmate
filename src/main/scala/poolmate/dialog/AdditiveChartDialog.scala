package poolmate.dialog

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

import poolmate.{App, Context, Model}

class AdditiveChartDialog(context: Context, model: Model) extends Dialog[Unit]:
  val additives = model.additiveList
  
  val years = additives.map(a => a.on.format(dateFormatter)).distinct

  val xAxis = CategoryAxis(years)
  xAxis.label = context.additiveChartAdditives

  val yAxis = NumberAxis(axisLabel = context.additiveChartAmounts, lowerBound = 0.0, upperBound = 100.00, tickUnit = 10.00)

  val chart = BarChart[String, Number](xAxis, yAxis)
  chart.categoryGap = 25.0
  chart.padding = Insets(6)

  val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
  years foreach { year =>
    val filteredYear = additives.filter(s => year == s.on.format(dateFormatter))
    val groupedChemicals = filteredYear.groupBy(_.chemical)
    groupedChemicals foreach { group =>
      val (chemical, additivesByChemical) = group
      val series = new XYChart.Series[String, Number]:
        name = chemical
        data() += XYChart.Data[String, Number](year, additivesByChemical.map(_.amount).sum)
      chart.data() += series
    }
  }

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox:
    spacing = 6; children = List(chart)

  initOwner(App.stage)
  title = context.additiveChart
  headerText = context.additiveAmounts