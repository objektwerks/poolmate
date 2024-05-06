package poolmate.dialog

import java.time.LocalDate

object Dialogs:
  given Ordering[LocalDate] = Ordering.by(_.toEpochDay)

  def isNotNumeric(text: String): Boolean = !text.matches("\\d{0,7}([\\.]\\d{0,4})?")