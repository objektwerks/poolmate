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

  val pools = config.getString("pools")
  val poolBuilt = config.getString("pool-built")
  val poolGallons = config.getString("pool-gallons")
  val poolStreet = config.getString("pool-street")
  val poolCity = config.getString("pool-city")
  val poolState = config.getString("pool-state")
  val poolZip = config.getString("pool-zip")
  val poolHeaderBuilt = config.getString("pool-header-built")
  val poolHeaderGallons = config.getString("pool-header-gallons")
  val poolHeaderStreet = config.getString("pool-header-street")
  val poolHeaderCity = config.getString("pool-header-city")
  val poolHeaderState = config.getString("pool-header-state")
  val poolHeaderZip = config.getString("pool-header-zip")

  val owners = config.getString("owners")
  val ownerSince = config.getString("owner-since")
  val ownerFirst = config.getString("owner-first")
  val ownerLast = config.getString("owner-last")
  val ownerEmail = config.getString("owner-email")
  val ownerHeaderSince = config.getString("owner-header-since")
  val ownerHeaderFirst = config.getString("owner-header-first")
  val ownerHeaderLast = config.getString("owner-header-last")
  val ownerHeaderEmail = config.getString("owner-header-email")

  val surfaces = config.getString("surfaces")
  val surfaceInstalled = config.getString("surface-installed")
  val surfaceKind = config.getString("surface-kind")
  val surfaceHeadedInstalled = config.getString("surface-header-installed")
  val surfaceHeaderKind = config.getString("surface-header-kind")

  val pumps = config.getString("pumps")
  val pumpInstalled = config.getString("pump-installed")
  val pumpModel = config.getString("pump-model")
  val pumpHeaderInstalled = config.getString("pump-header-installed")
  val pumpHeaderModel = config.getString("pump-header-model")

  val timers = config.getString("timers")
  val timerInstalled = config.getString("timer-installed")
  val timerModel = config.getString("timer-model")
  val timerHeaderInstalled = config.getString("timer-header-installed")
  val timerHeaderModel = config.getString("timer-header-model")

  val heaters = config.getString("heaters")
  val heaterInstalled = config.getString("heater-installed")
  val heaterModel = config.getString("heater-model")
  val heaterHeaderInstalled = config.getString("heater-header-installed")
  val heaterHeaderModel = config.getString("heater-header-model")

/* 
lifecycles = "Lifecycles"
lifecycle-created = "Created:"
lifecycle-active = "Active:"
lifecycle-pump-on = "Pump On:"
lifecycle-pump-off = "Pump Off:"
lifecycle-header-created = "Created"
lifecycle-header-active = "Active"
lifecycle-header-pump-on = "Pump On"
lifecycle-header-pump-off = "Pump Off"
 */
  val lifecycles = config.getString("lifecycles")
  val lifecycleCreated = config.getString("lifecycle-created")
  val lifecycleActive = config.getString("lifecycle-active")
  val lifecyclePumpOn = config.getString("lifecycle-pump-on")
  val lifecyclePumpOff = config.getString("lifecycle-pump-off")

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