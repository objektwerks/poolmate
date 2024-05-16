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

  val lifecycles = config.getString("lifecycles")
  val lifecycleCreated = config.getString("lifecycle-created")
  val lifecycleActive = config.getString("lifecycle-active")
  val lifecyclePumpOn = config.getString("lifecycle-pump-on")
  val lifecyclePumpOff = config.getString("lifecycle-pump-off")
  val lifecycleHeaderCreated = config.getString("lifecycle-header-created")
  val lifecycleHeaderActive = config.getString("lifecycle-header-active")
  val lifecycleHeaderPumpOn = config.getString("lifecycle-header-pump-on")
  val lifecycleHeaderPumpOff = config.getString("lifecycle-header-pump-off")

  val cleanings = config.getString("cleanings")
  val cleaningOn = config.getString("cleaning-on")
  val cleaningDeck = config.getString("cleaning-deck")
  val cleaningBrush = config.getString("cleaning-brush")
  val cleaningNet = config.getString("cleaning-net")
  val cleaningVacuum = config.getString("cleaning-vacuum")
  val cleaningSkimmerBasket = config.getString("cleaning-skimmer-basket")
  val cleaningPumpBasket = config.getString("cleaning-pump-basket")
  val cleaningPumpFilter = config.getString("cleaning-pump-filter")
  val cleaningHeaderOn = config.getString("cleaning-header-on")
  val cleaningHeaderDeck = config.getString("cleaning-header-deck")
  val cleaningHeaderBrush = config.getString("cleaning-header-brush")
  val cleaningHeaderNet = config.getString("cleaning-header-net")
  val cleaningHeaderVacuum = config.getString("cleaning-header-vacuum")
  val cleaningHeaderSkimmerBasket = config.getString("cleaning-header-skimmer-basket")
  val cleaningHeaderPumpBasket = config.getString("cleaning-header-pump-basket")
  val cleaningHeaderPumpFilter = config.getString("cleaning-header-pump-filter")

  val measurements = config.getString("measurements")
  val measurementOn = config.getString("measurement-on")
  val measurementTemp = config.getString("measurement-temp")
  val measurementHardness = config.getString("measurement-hardness")
  val measurementTotalChlorine = config.getString("measurement-total-chlorine")
  val measurementBromine = config.getString("measurement-bromine")
  val measurementFreeChlorine = config.getString("measurement-free-chlorine")
  val measurementPh = config.getString("measurement-ph")
  val measurementAlkalinity = config.getString("measurement-alkalinity")
  val measurementCyanuricAcid = config.getString("measurement-cyanuric-acid")
  val measurementHeaderOn = config.getString("measurement-header-on")
  val measurementHeaderTemp = config.getString("measurement-header-temp")
  val measurementHeaderHardness = config.getString("measurement-header-hardness")
  val measurementHeaderTotalChlorine = config.getString("measurement-header-total-chlorine")
  val measurementHeaderBromine = config.getString("measurement-header-bromine")
  val measurementHeaderFreeChlorine = config.getString("measurement-header-free-chlorine")
  val measurementHeaderPh = config.getString("measurement-header-ph")
  val measurementHeaderAlkalinity = config.getString("measurement-header-alkalinity")
  val measurementHeaderCyanuricAcid = config.getString("measurement-header-cyanuric-acid")
  val measurementChartOn = config.getString("measurement-chart-on")
  val measurementChartTemp = config.getString("measurement-chart-temp")
  val measurementChartHardness = config.getString("measurement-chart-hardness")
  val measurementChartTotalChlorine = config.getString("measurement-chart-total-chlorine")
  val measurementChartBromine = config.getString("measurement-chart-bromine")
  val measurementChartFreeChlorine = config.getString("measurement-chart-free-chlorine")
  val measurementChartPh = config.getString("measurement-chart-ph")
  val measurementChartAlkalinity = config.getString("measurement-chart-alkalinity")
  val measurementChartCyanuricAcid = config.getString("measurement-chart-cyanuric-acid")
  val measurementChartMeasurements = config.getString("measurement-chart-measurements")
  val measurementChartYearDay = config.getString("measurement-chart-year-day")
  val measurementChart = config.getString("measurement-chart")
  val measurementCharts = config.getString("measurement-charts")

  val additives = config.getString("additives")
  val additiveOn = config.getString("additive-on")
  val additiveChemical = config.getString("additive-chemical")
  val additiveUnit = config.getString("additive-unit")
  val additiveAmount = config.getString("additive-amount")
  val additiveHeaderOn = config.getString("additive-header-on")
  val additiveHeaderChemical = config.getString("additive-header-chemical")
  val additiveHeaderUnit = config.getString("additive-header-unit")
  val additiveHeaderAmount = config.getString("additive-header-amount")
  val additiveChartAdditives = config.getString("additive-chart-additives")
  val additiveChartAmounts = config.getString("additive-chart-amounts")
  val additiveChart = config.getString("additive-chart")
  val additiveAmounts = config.getString("additive-amounts")

  val supplies = config.getString("supplies")
  val supplyPurchased = config.getString("supply-purchased")
  val supplyItem = config.getString("supply-item")
  val supplyUnit = config.getString("supply-unit")
  val supplyAmount = config.getString("supply-amount")
  val supplyCost = config.getString("supply-cost")
  val supplyHeaderPurchased = config.getString("supply-header-purchased")
  val supplyHeaderItem = config.getString("supply-header-item")
  val supplyHeaderUnit = config.getString("supply-header-unit")
  val supplyHeaderAmount = config.getString("supply-header-amount")
  val supplyHeaderCost = config.getString("supply-header-cost")
  val supplyChartSupplies = config.getString("supply-chart-supplies")
  val supplyChartCosts = config.getString("supply-chart-costs")
  val supplyChart = config.getString("supply-chart")
  val supplyCosts = config.getString("supply-costs")

/*
repair-chart-year-day = "Year-Day"
repair-chart = "Repair Costs Chart"
repair-costs = "Repair Costs"
 */
  val repairs = config.getString("repairs")
  val repairOn = config.getString("repair-on")
  val repairItem = config.getString("repair-item")
  val repairCost = config.getString("repair-cost")
  val repairHeaderOn = config.getString("repair-header-on")
  val repairHeaderItem = config.getString("repair-header-item")
  val repairHeaderCost = config.getString("repair-header-cost")

  val repairChartCost = config.getString("repair-chart-cost")
  val repairChartCosts = config.getString("repair-chart-costs")
  val repairChartYearDay = config.getString("repair-chart-year-day")
  val repairChart = config.getString("repair-chart")
  val repairCosts = config.getString("repair-costs")


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