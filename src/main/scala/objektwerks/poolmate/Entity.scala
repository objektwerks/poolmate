package objektwerks.poolmate

import java.time.LocalDate

case class Owner(id: Int = 0, name: String, email: String, street: String, city: String, state: String, zip: Int)

case class Pool(id: Int = 0, ownerid: Int, gallons: Double = 1000.00)

case class Cleaning(id: Int = 0, poolid: Int, on: LocalDate = LocalDate.now, deck: Boolean = true, brush: Boolean = true, net: Boolean = true, basket: Boolean = true, filter: Boolean = true)

case class Measurement(id: Int = 0, poolid: Int, on: LocalDate = LocalDate.now, ch: Int = 5, ph: Int = 5, alky: Int = 5, temp: Int = 75)

case class Additive(id: Int = 0, poolid: Int, on: LocalDate = LocalDate.now, tablets: Int = 0, ch: Int = 0)

object Entity {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def ownerOrdering: Ordering[Owner] = Ordering.by(_.name)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(_.gallons)

  implicit def cleaningOrdering: Ordering[Cleaning] = Ordering.by(_.on)

  implicit def measurementOrdering: Ordering[Measurement] = Ordering.by(_.on)

  implicit def additiveOrdering: Ordering[Additive] = Ordering.by(_.on)
}