package objektwerks.poolmate.entity

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime}

import scalafx.beans.property.{IntegerProperty, StringProperty}

case class Pool(id: Int = 0, built: LocalDate = LocalDate.now, gallons: Int = 10000, street: String = "street",
                city: String = "city", state: String = "state", zip: Int = 12345) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
  val pBuilt = new StringProperty(this, "built", built.format(dateFormatter))
  val pGallons = new StringProperty(this, "gallons", gallons.toString)
  val pStreet = new StringProperty(this, "street", street)
  val pCity = new StringProperty(this, "city", city)
  val pState = new StringProperty(this, "state", state)
  val pZip = new StringProperty(this, "zip", zip.toString)

  val pool = this
}

case class Owner(id: Int = 0, poolId: Int, since: LocalDate, first: String, last: String, email: String) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Surface(id: Int = 0, poolId: Int, installed: LocalDate, kind: String) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Pump(id: Int = 0, poolId: Int, installed: LocalDate, model: String) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Timer(id: Int = 0, poolId: Int, installed: LocalDate, model: String) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Heater(id: Int = 0, poolId: Int, installed: LocalDate, model: String) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Lifecycle(id: Int = 0, poolId: Int, created: LocalDate = LocalDate.now, active: Boolean = true,
                     pumpOn: LocalTime, pumpOff: LocalTime) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
  val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")

  val pId = new IntegerProperty(this, "id", id)
}

case class Cleaning(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, deck: Boolean = true, brush: Boolean = true,
                    net: Boolean = true, vacuum: Boolean = false, skimmerBasket: Boolean = true, pumpBasket: Boolean = false,
                    pumpFilter: Boolean = false) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Measurement(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, temp: Int = 75, hardness: Int = 375,
                       totalChlorine: Int = 3, bromine: Int = 5, freeChlorine: Int = 3, pH: Double = 7.5,
                       alkalinity: Int = 100, cyanuricAcid: Int = 50) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Additive(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, chemical: String, unit: String, amount: Double) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

case class Repair(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, cost: Double, description: String) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val pId = new IntegerProperty(this, "id", id)
}

object Entity {
  implicit def localTimeOrdering: Ordering[LocalTime] = Ordering.by(_.toSecondOfDay)

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(p => (p.zip, p.city))

  implicit def ownerOrdering: Ordering[Owner] = Ordering.by(o => (o.since, o.last))

  implicit def surfaceOrdering: Ordering[Surface] = Ordering.by(_.installed)

  implicit def pumpOrdering: Ordering[Pump] = Ordering.by(_.installed)

  implicit def timerOrdering: Ordering[Timer] = Ordering.by(_.installed)

  implicit def heaterOrdering: Ordering[Heater] = Ordering.by(_.installed)

  implicit def lifecycleOrdering: Ordering[Lifecycle] = Ordering.by(l => (l.active, l.created))

  implicit def cleaningOrdering: Ordering[Cleaning] = Ordering.by(_.on)

  implicit def measurementOrdering: Ordering[Measurement] = Ordering.by(_.on)

  implicit def additiveOrdering: Ordering[Additive] = Ordering.by(_.on)

  implicit def repairOrdering: Ordering[Repair] = Ordering.by(_.on)
}