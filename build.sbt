name := "poolmate"
organization := "objektwerks"
version := "1.7-SNAPSHOT"
scalaVersion := "2.13.11"
libraryDependencies ++= {
  val slickVersion = "3.5.0-M4" // Still waiting to upgrade to Scala 3!
  Seq(
    "org.scalafx" %% "scalafx" % "20.0.0-R31",
    "org.jfxtras" % "jfxtras-controls" % "17-r1",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.1.220",
    "ch.qos.logback" % "logback-classic" % "1.4.8",
    "org.scalatest" %% "scalatest" % "3.2.16" % Test
  )
}
