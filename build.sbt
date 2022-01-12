name := "poolmate"
organization := "objektwerks"
version := "1.3-SNAPSHOT"
scalaVersion := "2.13.7"
libraryDependencies ++= {
  val slickVersion = "3.3.3"
  Seq(
    "org.scalafx" %% "scalafx" % "17.0.1-R26",
    "org.jfxtras" % "jfxtras-controls" % "15-r2",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.0.206",
    "ch.qos.logback" % "logback-classic" % "1.2.10",
    "org.scalatest" %% "scalatest" % "3.2.10" % Test
  )
}
