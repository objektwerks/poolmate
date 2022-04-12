package poolmate

import scalafx.collections.ObservableBuffer
import scalafx.beans.property.IntegerProperty

object Model {
  def apply(repository: Repository): Model = new Model(repository)
}

class Model(repository: Repository) {
  import repository._

  val poolList = ObservableBuffer[Pool]()
  val selectedPoolId = IntegerProperty(0)

  val ownerList = ObservableBuffer[Owner]()
  val selectedOwnerId = IntegerProperty(0)

  val surfaceList = ObservableBuffer[Surface]()
  val selectedSurfaceId = IntegerProperty(0)

  val pumpList = ObservableBuffer[Pump]()
  val selectedPumpId = IntegerProperty(0)

  val timerList = ObservableBuffer[Timer]()
  val selectedTimerId = IntegerProperty(0)

  val heaterList = ObservableBuffer[Heater]()
  val selectedHeaterId = IntegerProperty(0)

  val lifecycleList = ObservableBuffer[Lifecycle]()
  val selectedLifecycleId = IntegerProperty(0)

  val cleaningList = ObservableBuffer[Cleaning]()
  val selectedCleaningId = IntegerProperty(0)

  val measurementList = ObservableBuffer[Measurement]()
  val selectedMeasurementId = IntegerProperty(0)

  val additiveList = ObservableBuffer[Additive]()
  val selectedAdditiveId = IntegerProperty(0)

  val supplyList = ObservableBuffer[Supply]()
  val selectedSupplyId = IntegerProperty(0)
  
  val repairList = ObservableBuffer[Repair]()
  val selectedRepairId = IntegerProperty(0)

  def listPools(): Unit = {
    poolList.clear()
    ownerList.clear()
    surfaceList.clear()
    pumpList.clear()
    timerList.clear()
    heaterList.clear()
    lifecycleList.clear()
    cleaningList.clear()
    measurementList.clear()
    additiveList.clear()
    supplyList.clear()
    repairList.clear()
    poolList ++= await(pools.list())
  }

  def addPool(pool: Pool): Pool = {
    val newId = await(pools.save(pool))
    val newPool = pool.copy(id = newId.get)
    poolList += newPool
    selectedPoolId.value = newPool.id
    newPool
  }

  def updatePool(selectedIndex: Int, pool: Pool): Unit = {
    await(pools.save(pool))
    poolList.update(selectedIndex, pool)
    poolList.sorted
    ()
  }

  def listOwners(poolId: Int): Unit = {
    ownerList.clear()
    ownerList ++= await(owners.list(poolId))
  }

  def addOwner(owner: Owner): Owner = {
    val newId = await(owners.save(owner))
    val newOwner = owner.copy(id = newId.get)
    ownerList += newOwner
    selectedOwnerId.value = newOwner.id
    ownerList.sorted.reverse
    newOwner
  }

  def updateOwner(selectedIndex: Int, owner: Owner): Unit = {
    await(owners.save(owner))
    ownerList.update(selectedIndex, owner)
    ownerList.sorted.reverse
    ()
  }

  def listSurfaces(poolId: Int): Unit = {
    surfaceList.clear()
    surfaceList ++= await(surfaces.list(poolId))
  }

  def addSurface(surface: Surface): Surface = {
    val newId = await(surfaces.save(surface))
    val newSurface = surface.copy(id = newId.get)
    surfaceList += newSurface
    selectedSurfaceId.value = newSurface.id
    surfaceList.sorted.reverse
    newSurface
  }

  def updateSurface(selectedIndex: Int, surface: Surface): Unit = {
    await(surfaces.save(surface))
    surfaceList.update(selectedIndex, surface)
    surfaceList.sorted.reverse
    ()
  }

  def listPumps(poolId: Int): Unit = {
    pumpList.clear()
    pumpList ++= await(pumps.list(poolId))
  }

  def addPump(pump: Pump): Pump = {
    val newId = await(pumps.save(pump))
    val newPump = pump.copy(id = newId.get)
    pumpList += newPump
    selectedPumpId.value = newPump.id
    pumpList.sorted.reverse
    newPump
  }

  def updatePump(selectedIndex: Int, pump: Pump): Unit = {
    await(pumps.save(pump))
    pumpList.update(selectedIndex, pump)
    pumpList.sorted.reverse
    ()
  }

  def listTimers(poolId: Int): Unit = {
    timerList.clear()
    timerList ++= await(timers.list(poolId))
  }

  def addTimer(timer: Timer): Timer = {
    val newId = await(timers.save(timer))
    val newTimer = timer.copy(id = newId.get)
    timerList += newTimer
    selectedTimerId.value = newTimer.id
    timerList.sorted.reverse
    newTimer
  }

  def updateTimer(selectedIndex: Int, timer: Timer): Unit = {
    await(timers.save(timer))
    timerList.update(selectedIndex, timer)
    timerList.sorted.reverse
    ()
  }

  def listHeaters(poolId: Int): Unit = {
    heaterList.clear()
    heaterList ++= await(heaters.list(poolId))
  }

  def addHeater(heater: Heater): Heater = {
    val newId = await(heaters.save(heater))
    val newHeater = heater.copy(id = newId.get)
    heaterList += newHeater
    selectedHeaterId.value = newHeater.id
    heaterList.sorted.reverse
    newHeater
  }

  def updateHeater(selectedIndex: Int, heater: Heater): Unit = {
    await(heaters.save(heater))
    heaterList.update(selectedIndex, heater)
    heaterList.sorted.reverse
    ()
  }

  def listLifecycles(poolId: Int): Unit = {
    lifecycleList.clear()
    lifecycleList ++= await(lifecycles.list(poolId))
  }

  def addLifecycle(lifecycle: Lifecycle): Lifecycle = {
    val newId = await(lifecycles.save(lifecycle))
    val newLifecycle = lifecycle.copy(id = newId.get)
    lifecycleList += newLifecycle
    selectedLifecycleId.value = newLifecycle.id
    lifecycleList.sorted.reverse
    newLifecycle
  }

  def updateLifecycle(selectedIndex: Int, lifecycle: Lifecycle): Unit = {
    await(lifecycles.save(lifecycle))
    lifecycleList.update(selectedIndex, lifecycle)
    lifecycleList.sorted.reverse
    ()
  }

  def listCleanings(poolId: Int): Unit = {
    cleaningList.clear()
    cleaningList ++= await(cleanings.list(poolId))
  }

  def addCleaning(cleaning: Cleaning): Cleaning = {
    val newId = await(cleanings.save(cleaning))
    val newCleaning = cleaning.copy(id = newId.get)
    cleaningList += newCleaning
    selectedCleaningId.value = newCleaning.id
    cleaningList.sorted.reverse
    newCleaning
  }

  def updateCleaning(selectedIndex: Int, cleaning: Cleaning): Unit = {
    await(cleanings.save(cleaning))
    cleaningList.update(selectedIndex, cleaning)
    cleaningList.sorted.reverse
    ()
  }

  def listMeasurements(poolId: Int): Unit = {
    measurementList.clear()
    measurementList ++= await(measurements.list(poolId))
  }

  def addMeasurement(measurement: Measurement): Measurement = {
    val newId = await(measurements.save(measurement))
    val newMeasurement = measurement.copy(id = newId.get)
    measurementList += newMeasurement
    selectedMeasurementId.value = newMeasurement.id
    measurementList.sorted.reverse
    newMeasurement
  }

  def updateMeasurement(selectedIndex: Int, measurement: Measurement): Unit = {
    await(measurements.save(measurement))
    measurementList.update(selectedIndex, measurement)
    measurementList.sorted.reverse
    ()
  }

  def listAdditives(poolId: Int): Unit = {
    additiveList.clear()
    additiveList ++= await(additives.list(poolId))
  }

  def addAdditive(additive: Additive): Additive = {
    val newId = await(additives.save(additive))
    val newAdditive = additive.copy(id = newId.get)
    additiveList += newAdditive
    selectedAdditiveId.value = newAdditive.id
    additiveList.sorted.reverse
    newAdditive
  }

  def updateAdditive(selectedIndex: Int, additive: Additive): Unit = {
    await(additives.save(additive))
    additiveList.update(selectedIndex, additive)
    additiveList.sorted.reverse
    ()
  }

  def listSupplies(poolId: Int): Unit = {
    supplyList.clear()
    supplyList ++= await(supplies.list(poolId))
  }

  def addSupply(supply: Supply): Supply = {
    val newId = await(supplies.save(supply))
    val newSupply = supply.copy(id = newId.get)
    supplyList += newSupply
    selectedSupplyId.value = newSupply.id
    supplyList.sorted.reverse
    newSupply
  }

  def updateSupply(selectedIndex: Int, supply: Supply): Unit = {
    await(supplies.save(supply))
    supplyList.update(selectedIndex, supply)
    supplyList.sorted.reverse
    ()
  }

  def listRepairs(poolId: Int): Unit = {
    repairList.clear()
    repairList ++= await(repairs.list(poolId))
  }

  def addRepair(repair: Repair): Repair = {
    val newId = await(repairs.save(repair))
    val newRepair = repair.copy(id = newId.get)
    repairList += newRepair
    selectedRepairId.value = newRepair.id
    repairList.sorted.reverse
    newRepair
  }

  def updateRepair(selectedIndex: Int, repair: Repair): Unit = {
    await(repairs.save(repair))
    repairList.update(selectedIndex, repair)
    repairList.sorted.reverse
    ()
  }
}