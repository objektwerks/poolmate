package poolmate

import java.time.{LocalDate, LocalTime}

import scalafx.beans.property.StringProperty

object Entity:
  given Ordering[Pool] = Ordering.by(p => (p.zip, p.city))
  given Ordering[Owner] = Ordering.by(o => (o.since, o.last))
  given Ordering[Surface] = Ordering.by(_.installed)
  given Ordering[Pump] = Ordering.by(_.installed)
  given Ordering[Timer] = Ordering.by(_.installed)
  given Ordering[Heater] = Ordering.by(_.installed)
  given Ordering[Lifecycle] = Ordering.by(l => (l.active, l.created))
  given Ordering[Cleaning] = Ordering.by(_.on)
  given Ordering[Measurement] = Ordering.by(_.on)
  given Ordering[Additive] = Ordering.by(_.on)
  given Ordering[Supply] = Ordering.by(_.purchased)
  given Ordering[Repair] = Ordering.by(_.on)

  def toLocalDate(date: String): LocalDate = LocalDate.parse(date)
  def toLocalTime(time: String): LocalTime = LocalTime.parse(time)

sealed trait Entity:
  val id: Int

final case class Pool(id: Int = 0, 
                      built: String = LocalDate.now.toString, 
                      gallons: Int = 10000, 
                      street: String = "street", 
                      city: String = "city", 
                      state: String = "state", 
                      zip: Int = 12345) extends Entity derives CanEqual:
  val builtProperty = new StringProperty(this, "built", built)
  val gallonsProperty = new StringProperty(this, "gallons", gallons.toString)
  val streetProperty = new StringProperty(this, "street", street)
  val cityProperty = new StringProperty(this, "city", city)
  val stateProperty = new StringProperty(this, "state", state)
  val zipProperty = new StringProperty(this, "zip", zip.toString)
  val pool = this

final case class Owner(id: Int = 0, 
                       poolId: Int, 
                       since: String = LocalDate.now.toString, 
                       first: String = "first", 
                       last: String = "last", 
                       email: String = "your@email.org") extends Entity derives CanEqual:
  val sinceProperty = new StringProperty(this, "since", since)
  val firstProperty = new StringProperty(this, "first", first)
  val lastProperty = new StringProperty(this, "last", last)
  val emailProperty = new StringProperty(this, "email", email)
  val owner = this

final case class Surface(id: Int = 0, 
                         poolId: Int, 
                         installed: String = LocalDate.now.toString, 
                         kind: String = "kind") extends Entity derives CanEqual:
  val installedProperty = new StringProperty(this, "installed", installed)
  val kindProperty = new StringProperty(this, "kind", kind)
  val surface = this

final case class Pump(id: Int = 0, 
                      poolId: Int, 
                      installed: String = LocalDate.now.toString, 
                      model: String = "model") extends Entity derives CanEqual:
  val installedProperty = new StringProperty(this, "installed", installed)
  val modelProperty = new StringProperty(this, "model", model)
  val pump = this

final case class Timer(id: Int = 0, 
                       poolId: Int, 
                       installed: String = LocalDate.now.toString, 
                       model: String = "model") extends Entity derives CanEqual:
  val installedProperty = new StringProperty(this, "installed", installed)
  val modelProperty = new StringProperty(this, "model", model)
  val timer = this

final case class Heater(id: Int = 0, 
                        poolId: Int, 
                        installed: String = LocalDate.now.toString, 
                        model: String = "model") extends Entity derives CanEqual:
  val installedProperty = new StringProperty(this, "installed", installed)
  val modelProperty = new StringProperty(this, "model", model)
  val heater = this

final case class Lifecycle(id: Int = 0, 
                           poolId: Int, 
                           created: String = LocalDate.now.toString,
                           active: Boolean = true, 
                           pumpOn: String = LocalTime.of(9, 0).toString, 
                           pumpOff: String = LocalTime.of(17, 0).toString) extends Entity derives CanEqual:
  val createdProperty = new StringProperty(this, "created", created)
  val activeProperty = new StringProperty(this, "active", active.toString)
  val pumpOnProperty = new StringProperty(this, "pumpOn", pumpOn)
  val pumpOffProperty = new StringProperty(this, "pumpOff", pumpOff)
  val lifecycle = this

final case class Cleaning(id: Int = 0, 
                          poolId: Int, 
                          on: String = LocalDate.now.toString, 
                          deck: Boolean = true, 
                          brush: Boolean = true, 
                          net: Boolean = true,
                          vacuum: Boolean = false, 
                          skimmerBasket: Boolean = true, 
                          pumpBasket: Boolean = false, 
                          pumpFilter: Boolean = false) extends Entity derives CanEqual:
  val onProperty = new StringProperty(this, "on", on)
  val deckProperty = new StringProperty(this, "deck", deck.toString)
  val brushProperty = new StringProperty(this, "brush", brush.toString)
  val netProperty = new StringProperty(this, "net", net.toString)
  val vacuumProperty = new StringProperty(this, "vaccum", vacuum.toString)
  val skimmerBasketProperty = new StringProperty(this, "skimmerBasket", skimmerBasket.toString)
  val pumpBasketProperty = new StringProperty(this, "pumpBasket", pumpBasket.toString)
  val pumpFilterProperty = new StringProperty(this, "pumpFilter", pumpFilter.toString)
  val cleaning = this

final case class Measurement(id: Int = 0, 
                             poolId: Int, 
                             on: String = LocalDate.now.toString, 
                             temp: Double = 75.0, 
                             hardness: Double = 375.0, 
                             totalChlorine: Double = 3,
                             bromine: Double = 5.0, 
                             freeChlorine: Double = 3.0, 
                             pH: Double = 7.5, 
                             alkalinity: Double = 100.0, 
                             cyanuricAcid: Double = 50.0) extends Entity derives CanEqual:
  val onProperty = new StringProperty(this, "on", on)
  val tempProperty = new StringProperty(this, "temp", temp.toString)
  val hardnessProperty = new StringProperty(this, "hardness", hardness.toString)
  val totalChlorineProperty = new StringProperty(this, "totalChlorine", totalChlorine.toString)
  val bromineProperty = new StringProperty(this, "bromine", bromine.toString)
  val freeChlorineProperty = new StringProperty(this, "freeChlorine", freeChlorine.toString)
  val pHProperty = new StringProperty(this, "pH", pH.toString)
  val alkalinityProperty = new StringProperty(this, "alkalinity", alkalinity.toString)
  val cyanuricAcidProperty = new StringProperty(this, "cyanuricAcid", cyanuricAcid.toString)
  val measurement = this

final case class Additive(id: Int = 0, 
                          poolId: Int, 
                          on: String = LocalDate.now.toString, 
                          chemical: String = "cl", 
                          unit: String = "gl", 
                          amount: Double = 1.0) extends Entity derives CanEqual:
  val onProperty = new StringProperty(this, "on", on)
  val chemicalProperty = new StringProperty(this, "chemical", chemical)
  val unitProperty = new StringProperty(this, "unit", unit)
  val amountProperty = new StringProperty(this, "amount", amount.toString)
  val additive = this

final case class Supply(id: Int = 0, 
                        poolId: Int, 
                        purchased: String = LocalDate.now.toString, 
                        item: String = "cl", 
                        unit: String = "gl", 
                        amount: Double = 1.0, 
                        cost: Double = 0.0) extends Entity derives CanEqual:
  val purchasedProperty = new StringProperty(this, "purchased", purchased)
  val itemProperty = new StringProperty(this, "item", item)
  val unitProperty = new StringProperty(this, "unit", unit)
  val amountProperty = new StringProperty(this, "amount", amount.toString)
  val costProperty = new StringProperty(this, "cost", cost.toString)
  val supply = this

final case class Repair(id: Int = 0, 
                        poolId: Int, 
                        on: String = LocalDate.now.toString, 
                        item: String = "repair", 
                        cost: Double = 0.0) extends Entity derives CanEqual:
  val onProperty = new StringProperty(this, "on", on)
  val itemProperty = new StringProperty(this, "item", item)
  val costProperty = new StringProperty(this, "cost", cost.toString)
  val repair = this