package objektwerks.poolmate.pane

import com.typesafe.config.Config
import objektwerks.poolmate.Resources._
import objektwerks.poolmate.dialog.SurfaceDialog
import objektwerks.poolmate.entity.Surface
import objektwerks.poolmate.model.Model

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

class SurfacePane(conf: Config, model: Model) extends VBox {
  val surfaceTableView = new TableView[Surface]() {
    columns ++= List(
      new TableColumn[Surface, String] { text = conf.getString("surface-header-installed"); cellValueFactory = { _.value.installedProperty } },
      new TableColumn[Surface, String] { text = conf.getString("surface-header-kind"); cellValueFactory = { _.value.kindProperty } }
    )
    items = model.surfaceList
  }
  surfaceTableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  val surfaceAddButton = new Button { graphic = addImageView(); disable = true }
  val surfaceEditButton = new Button { graphic = editImageView(); disable = true }
  val surfaceToolBar = new HBox { spacing = 6; children = List(surfaceAddButton, surfaceEditButton) }

  spacing = 6
  padding = Insets(6)
  children = List(surfaceTableView, surfaceToolBar)

  model.selectedPoolId.onChange { (_, _, selectedPoolId) =>
    model.listSurfaces(selectedPoolId.intValue)
    surfaceAddButton.disable = false
  }

  surfaceTableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedSurface) =>
    // model.update executes a remove and add on items. the remove passes a null selectedSurface!
    if (selectedSurface != null) {
      model.selectedSurfaceId.value = selectedSurface.id
      surfaceEditButton.disable = false
    }
  }

  surfaceTableView.onMouseClicked = { event =>
    if(event.getClickCount == 2 && surfaceTableView.selectionModel().getSelectedItem != null ) update()
  }

  surfaceAddButton.onAction = { _ => add() }

  surfaceEditButton.onAction = { _ => update() }

  def add(): Unit = {
    new SurfaceDialog(conf, Surface(poolId = model.selectedPoolId.toInt)).showAndWait() match {
      case Some(Surface(id, poolId, installed, kind)) =>
        val newSurface = model.addSurface(Surface(id, poolId, installed, kind))
        surfaceTableView.selectionModel().select(newSurface)
      case _ =>
    }
  }

  def update(): Unit = {
    val selectedIndex = surfaceTableView.selectionModel().getSelectedIndex
    val surface = surfaceTableView.selectionModel().getSelectedItem.surface
    new SurfaceDialog(conf, surface).showAndWait() match {
      case Some(Surface(id, poolId, installed, kind)) =>
        model.updateSurface(selectedIndex, Surface(id, poolId, installed, kind))
        surfaceTableView.selectionModel().select(selectedIndex)
      case _ =>
    }
  }
}