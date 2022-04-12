package poolmate

import java.time.LocalDate

package object dialog {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  def isNotNumeric(text: String): Boolean = !text.matches("\\d{0,7}([\\.]\\d{0,4})?")
}