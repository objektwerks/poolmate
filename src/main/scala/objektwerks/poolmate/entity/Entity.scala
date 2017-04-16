package objektwerks.poolmate.entity

import java.time.LocalDate

case class Owner(id: Int = 0, name: String, email: String, street: String, city: String, state: String, zip: Int)

case class Pool(id: Int = 0, ownerid: Int, gallons: Double = 1000.00)

case class Cleaning(id: Int = 0, poolid: Int, on: LocalDate = LocalDate.now, deck: Boolean = true, brush: Boolean = true,
                    net: Boolean = true, vacuum: Boolean = false, skimmerBasket: Boolean = true, pumpBasket: Boolean = false,
                    pumpFilter: Boolean = false)

case class Measurement(id: Int = 0, poolid: Int, on: LocalDate = LocalDate.now, temp: Int = 75, totalHardness: Int = 375,
                       totalChlorine: Int = 3, totalBromine: Int = 5, freeChlorine: Int = 3, pH: Double = 7.5, totalAlkalinity: Int = 100,
                       cyanuricAcid: Int = 50)

case class Chemical(id: Int = 0, name: String, unit: String)

case class Additive(id: Int = 0, poolid: Int, chemicalid: Int, on: LocalDate = LocalDate.now, amount: Double = 1.0)

case class Repair(id: Int = 0, poolid: Int, on: LocalDate = LocalDate.now, cost: Double = 0.00, description: String)

object Entity {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def ownerOrdering: Ordering[Owner] = Ordering.by(_.name)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(_.gallons)

  implicit def cleaningOrdering: Ordering[Cleaning] = Ordering.by(_.on)

  implicit def measurementOrdering: Ordering[Measurement] = Ordering.by(_.on)

  implicit def chemicalOrdering: Ordering[Chemical] = Ordering.by(_.name)

  implicit def additiveOrdering: Ordering[Additive] = Ordering.by(_.on)

  implicit def repairOrdering: Ordering[Repair] = Ordering.by(_.on)
}