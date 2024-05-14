package poolmate

import com.typesafe.config.Config

import scalafx.collections.ObservableBuffer
import scalafx.scene.image.{Image, ImageView}

import scala.jdk.CollectionConverters.*

class Context(config: Config):
  val title = config.getString("title")
  val developer = config.getString("developer")
  val license = config.getString("license")
  val app = config.getString("app")
  val height = config.getInt("height")
  val width = config.getInt("width")
  val menu = config.getString("app")
  val about = config.getString("about")
  val exit = config.getString("exit")
  val min = config.getString("min")
  val max = config.getString("max")
  val avg = config.getString("avg")

  /* 
  save = "Save"
  save-pool = "Save Pool"
  save-owner = "Save Owner"
  save-surface = "Save Surface"
  save-pump = "Save Pump"
  save-timer = "Save Timer"
  save-heater = "Save Heater"
  save-lifecycle = "Save Lifecycle"
  save-cleaning = "Save Cleaning"
  save-measurement = "Save Measurement"
  save-additive = "Save Additive"
  save-supply = "Save Supply"
  save-repair = "Save Repair"
   */

  val save = config.getString("save")
  val savePool = config.getString("save-pool")
  val saveOwner = config.getString("save-owner")
  val saveSurface = config.getString("save-surface")
  val savePump = config.getString("save-pump")
  val saveTimer = config.getString("save-timer")
  val saveHeater = config.getString("save-heater")
  val saveLifecycle = config.getString("save-lifecycle")
  val saveCleaning = config.getString("save-cleaning")
  val saveMeasurement = config.getString("save-measurement")
  val saveAdditive = config.getString("save-additive")
  val saveSupply = config.getString("save-supply")
  val saveRepair = config.getString("save-repair")

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