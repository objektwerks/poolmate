package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.Resources._
import objektwerks.poolmate.dialog.TimerDialog
import objektwerks.poolmate.entity.Timer
import objektwerks.poolmate.model.Model
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class TimerPane(conf: Config, model: Model) extends VBox {
  val timerTableView = new TableView[Timer]() {
    columns ++= List(
      new TableColumn[Timer, String] { text = conf.getString("timer-header-installed"); cellValueFactory = { _.value.installedProperty } },
      new TableColumn[Timer, String] { text = conf.getString("timer-header-model"); cellValueFactory = { _.value.modelProperty } }
    )
    items = model.timerList
  }
  timerTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val timerAddButton = new Button { graphic = addImageView(); disable = true }
  val timerEditButton = new Button { graphic = editImageView(); disable = true }
  val timerToolBar = new HBox { spacing = 6; children = List(timerAddButton, timerEditButton) }

  spacing = 6
  padding = Insets(6)
  children = List(timerTableView, timerToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listTimers(selectedPoolId.intValue)
    timerAddButton.disable = false
  }

  timerTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedTimer) =>
    // model.update executes a remove and add on items. the remove passes a null selectedTimer!
    if (selectedTimer != null) {
      model.selectedTimerId.value = selectedTimer.id
      timerEditButton.disable = false
    }
  }

  timerTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && timerTableView.selectionModel().getSelectedItem != null ) update()
  }

  timerAddButton.onAction = { _ => add() }

  timerEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new TimerDialog(conf, Timer(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Timer(id, poolId, installed, _model)) =>
        val newTimer = model.addTimer(Timer(id, poolId, installed, _model))
        timerTableView.selectionModel().select(newTimer)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = timerTableView.selectionModel().getSelectedIndex
    val timer = timerTableView.selectionModel().getSelectedItem.timer
    new TimerDialog(conf, timer).showAndWait() match {
      case Some(Timer(id, poolId, installed, _model)) =>
        model.updateTimer(selectedIndex, Timer(id, poolId, installed, _model))
        timerTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}