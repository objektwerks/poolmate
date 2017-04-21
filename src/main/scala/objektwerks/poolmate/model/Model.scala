package objektwerks.poolmate.model

import objektwerks.poolmate.entity._
import objektwerks.poolmate.entity.Entity._
import objektwerks.poolmate.repository.Repository

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer

class Model(repository: Repository) {
  import repository._

  val poolList = ObservableBuffer[Pool]()
  val selectedPoolId = ObjectProperty[Int](0)

  def listPools(): Unit = {
    poolList.clear()
    ownerList.clear()
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
  }

  val ownerList = ObservableBuffer[Owner]()
  val selectedOwnerId = ObjectProperty[Int](0)

  def listOwners(): Unit = {
    ownerList.clear()
    ownerList ++= await(owners.list())
  }

  def addOwner(owner: Owner): Owner = {
    val newId = await(owners.save(owner))
    val newOwner = owner.copy(id = newId.get)
    ownerList += newOwner
    selectedOwnerId.value = newOwner.id
    newOwner
  }

  def updateOwner(selectedIndex: Int, owner: Owner): Unit = {
    await(owners.save(owner))
    ownerList.update(selectedIndex, owner)
    ownerList.sorted
  }

  val surfaceList = ObservableBuffer[Surface]()
  val selectedSurfaceId = ObjectProperty[Int](0)


  val pumpList = ObservableBuffer[Pump]()
  val selectedPumpId = ObjectProperty[Int](0)

  val timerList = ObservableBuffer[Timer]()
  val selectedTimerId = ObjectProperty[Int](0)

  val heaterList = ObservableBuffer[Heater]()
  val selectedHeaterId = ObjectProperty[Int](0)

  val lifecycleList = ObservableBuffer[Lifecycle]()
  val selectedLifecycleId = ObjectProperty[Int](0)

  val cleaningList = ObservableBuffer[Cleaning]()
  val selectedCleaningId = ObjectProperty[Int](0)

  val measurementList = ObservableBuffer[Measurement]()
  val selectedMeasurementId = ObjectProperty[Int](0)

  val additiveList = ObservableBuffer[Additive]()
  val selectedAdditiveId = ObjectProperty[Int](0)

  val repairList = ObservableBuffer[Repair]()
  val selectedRepairId = ObjectProperty[Int](0)
}