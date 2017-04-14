package objektwerks.poolmate

import java.time.LocalDate

case class Pool(id: Int = 0, name: String = "default_student", born: LocalDate = LocalDate.now.minusYears(7))

case class Log(id: Int = 0, poolid: Int, year: String = "default_grade", started: LocalDate = LocalDate.now, completed: LocalDate = LocalDate.now.plusMonths(6))

case class Entry(id: Int = 0, logid: Int, name: String = "default_course", started: LocalDate = LocalDate.now, completed: LocalDate = LocalDate.now.plusMonths(3))

object Entity {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  implicit def poolOrdering: Ordering[Pool] = Ordering.by(_.born)

  implicit def logOrdering: Ordering[Log] = Ordering.by(_.started)

  implicit def entryOrdering: Ordering[Entry] = Ordering.by(_.started)
}