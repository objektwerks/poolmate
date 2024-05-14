package poolmate

import com.typesafe.config.Config

import scalafx.collections.ObservableBuffer
import scalafx.scene.image.{Image, ImageView}

import scala.jdk.CollectionConverters.*

class Context(config: Config):
  val title = config.getString("title")
  val developer = config.getString("developer")
  val license = config.getString("license")

  def units = ObservableBuffer[String]( config.getStringList("units").asScala.toSeq * )

  def appImage = new Image( Image.getClass.getResourceAsStream("/images/pool.png") )

  def addImageView = loadImageView("/images/add.png")

  def editImageView = loadImageView("/images/edit.png")

  def barChartImageView = loadImageView("/images/bar.chart.png")

  def lineChartImageView = loadImageView("/images/line.chart.png")

  def loadImageView(path: String): ImageView = new ImageView:
    image = new Image( Image.getClass.getResourceAsStream(path) )
    fitHeight = 25
    fitWidth = 25
    preserveRatio = true
    smooth = true