name := "poolmate"
organization := "objektwerks"
version := "1.3-SNAPSHOT"
scalaVersion := "2.13.5"
libraryDependencies ++= {
  val slickVersion = "3.3.3"
  Seq(
    "org.scalafx" %% "scalafx" % "15.0.1-R21",
    "org.jfxtras" % "jfxtras-controls" % "15-r1",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "1.4.200",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scalatest" %% "scalatest" % "3.2.8" % Test
  )
}
