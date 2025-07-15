package poolmate

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import java.awt.{Taskbar, Toolkit}
import java.awt.Taskbar.Feature

import scalafx.application.JFXApp3

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object App extends JFXApp3 with LazyLogging:
  val config = ConfigFactory.load("app.conf")
  val context = Context(config)

  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("repository", ConfigFactory.load("repository.conf"))
  val repository = Repository(dbConfig).ifAbsentInstall()
  val model = Model(repository)

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      scene = View(context, model).scene
      title = context.title
      icons += context.appImage

    if Taskbar.isTaskbarSupported() then
      val taskbar = Taskbar.getTaskbar()
      if taskbar.isSupported(Feature.ICON_IMAGE) then
        val appIcon = Toolkit.getDefaultToolkit.getImage(this.getClass().getResource("/images/icon.png"))
        taskbar.setIconImage(appIcon)

    model.listPools()
    stage.show()
    logger.info("*** PoolMate app started.")

  override def stopApp(): Unit =
    repository.close()
    logger.info("*** PoolMate app stopped.")