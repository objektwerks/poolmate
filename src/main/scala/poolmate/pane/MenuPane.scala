package poolmate.pane

import scalafx.application.Platform
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Menu, MenuBar, MenuItem, SeparatorMenuItem}

import poolmate.{App, Context}

class MenuPane(context: Context) extends MenuBar:
  val aboutDialog = new Alert(AlertType.Information):
    initOwner(App.stage)
    title = context.about
    headerText = context.developer
    contentText = s"${context.app} ${context.license}"

  val aboutMenuItem = new MenuItem(context.about):
    onAction = { _ => aboutDialog.showAndWait() }

  val separator = SeparatorMenuItem()

  val exitMenuItem = new MenuItem(context.exit):
    onAction = { _ => Platform.exit() }
  
  val menu = new Menu(context.menu):
    items = List(aboutMenuItem, separator, exitMenuItem)

  menus = List(menu)
  useSystemMenuBar = false