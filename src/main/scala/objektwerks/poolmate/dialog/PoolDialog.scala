package objektwerks.poolmate.dialog

import com.typesafe.config.Config
import objektwerks.poolmate.entity.Pool

import scalafx.scene.control.Dialog

class PoolDialog(conf: Config, pool: Pool) extends Dialog[Pool]()  {
  println(conf)
  println(pool)
}