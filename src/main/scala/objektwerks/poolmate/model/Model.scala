package objektwerks.poolmate.model

import objektwerks.poolmate.entity.Owner
import objektwerks.poolmate.entity.Entity._
import objektwerks.poolmate.repository.Repository

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer

class Model(repository: Repository) {
  import repository._

  val ownerList = ObservableBuffer[Owner]()
  val selectedOwnerId = ObjectProperty[Int](0)

  def listOwners(): Unit = {
    ownerList.clear()
    ownerList ++= await(owners.list())
  }

  def updateOwner(selectedIndex: Int, owner: Owner): Unit = {
    await(owners.save(owner))
    ownerList.update(selectedIndex, owner)
    ownerList.sorted
  }

  def addOwner(owner: Owner): Owner = {
    val newId = await(owners.save(owner))
    val newOwner = owner.copy(id = newId.get)
    ownerList += newOwner
    selectedOwnerId.value = newOwner.id
    newOwner
  }
}