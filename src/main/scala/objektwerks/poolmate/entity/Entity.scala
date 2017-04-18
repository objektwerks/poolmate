package objektwerks.poolmate.entity

import java.time.{LocalDate, LocalTime}

case class Location(id: Int = 0, street: String, city: String, state: String, zip: Int)

case class Owner(id: Int = 0, locationId: Int, poolId: Int, first: String, last: String, email: String)

case class Pool(id: Int = 0, currentOwnerId: Int, locationId: Int, gallons: Double, surface: String, pump: String, timer: String, heater: String)

case class Cleaning(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, deck: Boolean = true, brush: Boolean = true,
                    net: Boolean = true, vacuum: Boolean = false, skimmerBasket: Boolean = true, pumpBasket: Boolean = false,
                    pumpFilter: Boolean = false)

case class Measurement(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, temp: Int = 75, totalHardness: Int = 375,
                       totalChlorine: Int = 3, totalBromine: Int = 5, freeChlorine: Int = 3, pH: Double = 7.5, totalAlkalinity: Int = 100,
                       cyanuricAcid: Int = 50)

case class Additive(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, chemical: String, unit: String, amount: Double)

case class Repair(id: Int = 0, poolId: Int, on: LocalDate = LocalDate.now, cost: Double, description: String)

case class Timer(id: Int = 0, poolId: Int, on: LocalTime, off: LocalTime)

object Entity {
  implicit def localTimeOrdering: Ordering[LocalTime] = Ordering.by(_.toSecondOfDay)

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def locationOrdering: Ordering[Location] = Ordering.by(l => (l.city, l.state))

  implicit def ownerOrdering: Ordering[Owner] = Ordering.by(_.last)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(_.gallons)

  implicit def cleaningOrdering: Ordering[Cleaning] = Ordering.by(_.on)

  implicit def measurementOrdering: Ordering[Measurement] = Ordering.by(_.on)

  implicit def additiveOrdering: Ordering[Additive] = Ordering.by(_.on)

  implicit def repairOrdering: Ordering[Repair] = Ordering.by(_.on)

  implicit def timerOrdering: Ordering[Timer] = Ordering.by(_.on)
}