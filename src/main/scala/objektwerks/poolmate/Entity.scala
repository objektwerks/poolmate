package objektwerks.poolmate

import java.time.LocalDate

case class Pool(id: Int = 0, gallons: Double = 1000.00, owner: String = "owner")

case class Measurement(id: Int = 0, poolid: Int, created: LocalDate = LocalDate.now)

object Entity {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(_.owner)

  implicit def measurementOrdering: Ordering[Measurement] = Ordering.by(_.created)
}