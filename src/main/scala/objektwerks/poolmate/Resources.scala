package objektwerks.poolmate

import com.typesafe.config.ConfigFactory

import scalafx.collections.ObservableBuffer
import scalafx.scene.image.{Image, ImageView}

import scala.jdk.CollectionConverters._

object Resources {
  val conf = ConfigFactory.load("app.conf")
  def units = ObservableBuffer[String](conf.getStringList("units").asScala.toSeq)
  def appImage = new Image(Resources.getClass.getResourceAsStream("/images/pool.png"))
  def addImageView = loadImageView("/images/add.png")
  def editImageView = loadImageView("/images/edit.png")
  def barChartImageView = loadImageView("/images/bar.chart.png")
  def lineChartImageView = loadImageView("/images/line.chart.png")

  def loadImageView(path: String): ImageView = new ImageView {
    image = new Image(Resources.getClass.getResourceAsStream(path))
    fitHeight = 25
    fitWidth = 25
    preserveRatio = true
    smooth = true
  }
}