package objektwerks.poolmate

import java.time.LocalDate

case class Pool(id: Int = 0, owner: String = "default_owner")

case class Log(id: Int = 0, poolid: Int, created: LocalDate = LocalDate.now)

case class Entry(id: Int = 0, logid: Int, created: LocalDate = LocalDate.now)

object Entity {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(_.owner)

  implicit def logOrdering: Ordering[Log] = Ordering.by(_.created)

  implicit def entryOrdering: Ordering[Entry] = Ordering.by(_.created)
}