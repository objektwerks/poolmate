package objektwerks.poolmate.image

import scalafx.scene.image.{Image, ImageView}

object Images {
  def appImage(): Image = new Image(Images.getClass.getResourceAsStream("/images/pool.png"))

  def addImageView(): ImageView = loadImageView("/images/add.png")

  def editImageView(): ImageView = loadImageView("/images/edit.png")

  def barChartImageView(): ImageView = loadImageView("/images/bar.chart.png")

  def lineChartImageView(): ImageView = loadImageView("/images/line.chart.png")

  def loadImageView(path: String): ImageView = new ImageView {
    image = new Image(Images.getClass.getResourceAsStream(path))
    fitHeight = 25
    fitWidth = 25
    preserveRatio = true
    smooth = true
  }
}