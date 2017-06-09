package objektwerks.poolmate.entity

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, LocalTime, ZoneOffset}

import scalafx.beans.property.StringProperty

case class Company(id: Int = 0, name: String = "name", since: LocalDate = LocalDate.now, website: String = "website", email: String = "email")

case class Worker(id: Int = 0, companyId: Int, hired: LocalDate = LocalDate.now, terminated: Option[LocalDate], first: String = "first", last: String = "last", email: String)

case class WorkOrder(id: Int = 0, workerId: Int, poolId: Int, description: String = "description", created: LocalDate = LocalDate.now, completed: Option[LocalDate])

case class RouteOrder(id: Int = 0, routeId: Int, workerId: Int, created: LocalDate = LocalDate.now, recurring: Boolean = true)

case class Location(routeOrderId: Int, poolId: Int, ordinality: Int, completed: Option[LocalDateTime])

case class Route(id: Int = 0, name: String)

case class Stop(routeId: Int, poolId: Int, ordinality: Int)

case class Pool(id: Int = 0, built: LocalDate = LocalDate.now, gallons: Int = 10000, street: String = "street", city: String = "city", state: String = "state", zip: Int = 12345) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val builtProperty = new StringProperty(this, "built", built.format(dateFormatter))
  val gallonsProperty = new StringProperty(this, "gallons", gallons.toString)
  val streetProperty = new StringProperty(this, "street", street)
  val cityProperty = new StringProperty(this, "city", city)
  val stateProperty = new StringProperty(this, "state", state)
  val zipProperty = new StringProperty(this, "zip", zip.toString)

  val pool = this
}

case class Owner(id: Int = 0, poolId: Int, since: LocalDate = LocalDate.now, first: String = "first", last: String = "last", email: String = "your@email.org") {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val sinceProperty = new StringProperty(this, "since", since.format(dateFormatter))
  val firstProperty = new StringProperty(this, "first", first)
  val lastroperty = new StringProperty(this, "last", last)

  val owner = this
}

case class Surface(id: Int = 0, poolId: Int, installed: LocalDate = LocalDate.now, kind: String = "kind") {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val installedProperty = new StringProperty(this, "installed", installed.format(dateFormatter))
  val kindProperty = new StringProperty(this, "kind", kind)

  val surface = this
}

case class Pump(id: Int = 0, poolId: Int, installed: LocalDate = LocalDate.now, model: String = "model") {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val installedProperty = new StringProperty(this, "installed", installed.format(dateFormatter))
  val modelProperty = new StringProperty(this, "model", model)

  val pump = this
}

case class Timer(id: Int = 0, poolId: Int, installed: LocalDate = LocalDate.now, model: String = "model") {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val installedProperty = new StringProperty(this, "installed", installed.format(dateFormatter))
  val modelProperty = new StringProperty(this, "model", model)

  val timer = this
}

case class Heater(id: Int = 0, poolId: Int, installed: LocalDate = LocalDate.now, model: String = "model") {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val installedProperty = new StringProperty(this, "installed", installed.format(dateFormatter))
  val modelProperty = new StringProperty(this, "model", model)

  val heater = this
}

case class Lifecycle(id: Int = 0, poolId: Int, created: LocalDate = LocalDate.now, active: Boolean = true, pumpOn: LocalTime = LocalTime.of(9, 0), pumpOff: LocalTime = LocalTime.of(17, 0)) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
  val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")

  val createdProperty = new StringProperty(this, "created", created.format(dateFormatter))
  val activeProperty = new StringProperty(this, "active", active.toString)
  val pumpOnProperty = new StringProperty(this, "pumpOn", pumpOn.format(timeFormatter))
  val pumpOffProperty = new StringProperty(this, "pumpOff", pumpOff.format(timeFormatter))

  val lifecycle = this
}

case class Cleaning(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, deck: Boolean = true, brush: Boolean = true, net: Boolean = true,
                    vacuum: Boolean = false, skimmerBasket: Boolean = true, pumpBasket: Boolean = false, pumpFilter: Boolean = false) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val onProperty = new StringProperty(this, "on", on.format(dateFormatter))
  val deckProperty = new StringProperty(this, "deck", deck.toString)
  val brushProperty = new StringProperty(this, "brush", brush.toString)
  val netProperty = new StringProperty(this, "net", net.toString)
  val vacuumProperty = new StringProperty(this, "vaccum", vacuum.toString)
  val skimmerBasketProperty = new StringProperty(this, "skimmerBasket", skimmerBasket.toString)
  val pumpBasketProperty = new StringProperty(this, "pumpBasket", pumpBasket.toString)
  val pumpFilterProperty = new StringProperty(this, "pumpFilter", pumpFilter.toString)

  val cleaning = this
}

case class Measurement(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, temp: Int = 75, hardness: Int = 375, totalChlorine: Int = 3,
                       bromine: Int = 5, freeChlorine: Int = 3, pH: Double = 7.5, alkalinity: Int = 100, cyanuricAcid: Int = 50) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val onProperty = new StringProperty(this, "on", on.format(dateFormatter))
  val tempProperty = new StringProperty(this, "temp", temp.toString)
  val hardnessProperty = new StringProperty(this, "hardness", hardness.toString)
  val totalChlorineProperty = new StringProperty(this, "totalChlorine", totalChlorine.toString)
  val bromineProperty = new StringProperty(this, "bromine", bromine.toString)
  val freeChlorineProperty = new StringProperty(this, "freeChlorine", freeChlorine.toString)
  val pHProperty = new StringProperty(this, "pH", pH.toString)
  val alkalinityProperty = new StringProperty(this, "alkalinity", alkalinity.toString)
  val cyanuricAcidProperty = new StringProperty(this, "cyanuricAcid", cyanuricAcid.toString)

  val measurement = this
}

case class Additive(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, chemical: String = "cl", unit: String = "gl", amount: Double = 1.0) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val onProperty = new StringProperty(this, "on", on.format(dateFormatter))
  val chemicalProperty = new StringProperty(this, "chemical", chemical)
  val unitProperty = new StringProperty(this, "unit", unit)
  val amountProperty = new StringProperty(this, "amount", amount.toString)

  val additive = this
}

case class Supply(id: Int = 0, poolId: Int, purchased: LocalDate = LocalDate.now, item: String = "cl", unit: String = "gl", amount: Double = 1.0, cost: Double = 0.0) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val purchasedProperty = new StringProperty(this, "purchased", purchased.format(dateFormatter))
  val itemProperty = new StringProperty(this, "item", item)
  val unitProperty = new StringProperty(this, "unit", unit)
  val amountProperty = new StringProperty(this, "amount", amount.toString)
  val costProperty = new StringProperty(this, "cost", cost.toString)

  val supply = this
}

case class Repair(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, item: String = "repair", cost: Double = 0.0) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  val onProperty = new StringProperty(this, "on", on.format(dateFormatter))
  val itemProperty = new StringProperty(this, "item", item)
  val costProperty = new StringProperty(this, "cost", cost.toString)

  val repair = this
}

object Entity {
  implicit def localTimeOrdering: Ordering[LocalTime] = Ordering.by(_.toSecondOfDay)

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def localDateTimeOrdering: Ordering[LocalDateTime] = Ordering.by(_.toEpochSecond(ZoneOffset.UTC))

  implicit def companyOrdering: Ordering[Company] = Ordering.by(_.name)

  implicit def workerOrdering: Ordering[Worker] = Ordering.by(_.last)

  implicit def workOrderOrdering: Ordering[WorkOrder] = Ordering.by(_.created)

  implicit def routeOrderOrdering: Ordering[RouteOrder] = Ordering.by(ro => (ro.created, ro.recurring))

  implicit def locationOrdering: Ordering[Location] = Ordering.by(_.ordinality)

  implicit def routeOrdering: Ordering[Route] = Ordering.by(_.name)

  implicit def stopOrdering: Ordering[Stop] = Ordering.by(_.ordinality)

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

  implicit def supplyOrdering: Ordering[Supply] = Ordering.by(_.purchased)

  implicit def repairOrdering: Ordering[Repair] = Ordering.by(_.on)
}