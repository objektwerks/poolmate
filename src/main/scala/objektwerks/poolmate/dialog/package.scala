package objektwerks.poolmate

package object dialog {
  def isNotNumeric(text: String): Boolean = !text.matches("\\d{0,7}([\\.]\\d{0,4})?")
}