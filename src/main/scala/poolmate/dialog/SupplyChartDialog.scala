package poolmate.dialog

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{BarChart, CategoryAxis, NumberAxis, XYChart}
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

import poolmate.{App, Context, Model}

class SupplyChartDialog(context: Context, model: Model) extends Dialog[Unit]:
  val supplies = model.supplyList

  val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
  val years = supplies.map(s => s.purchased.format(dateFormatter)).distinct

  val xAxis = CategoryAxis(years)
  xAxis.label = context.supplyChartSupplies

  val yAxis = NumberAxis(axisLabel = context.supplyChartCosts, lowerBound = 0.0, upperBound = 1000.00, tickUnit = 100.00)

  val chart = BarChart[String, Number](xAxis, yAxis)
  chart.categoryGap = 25.0
  chart.padding = Insets(6)

  years foreach { year =>
    val filteredYear = supplies.filter(s => year == s.purchased.format(dateFormatter))
    val groupedItems = filteredYear.groupBy(_.item)
    groupedItems foreach { group =>
      val (item, suppliesByItem) = group
      val series = new XYChart.Series[String, Number]:
        name = item
        data() += XYChart.Data[String, Number](year, suppliesByItem.map(_.cost).sum)
      chart.data() += series
    }
  }

  val dialog = dialogPane()
  dialog.buttonTypes = List(ButtonType.Close)
  dialog.content = new VBox:
    spacing = 6; children = List(chart)

  initOwner(App.stage)
  title = context.supplyChart
  headerText = context.supplyCosts