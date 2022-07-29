name := "poolmate"
organization := "objektwerks"
version := "1.7-SNAPSHOT"
scalaVersion := "2.13.8"
libraryDependencies ++= {
  val slickVersion = "3.3.3" // Can't yet upgrade to Scala 3!
  Seq(
    "org.scalafx" %% "scalafx" % "18.0.2-R29",
    "org.jfxtras" % "jfxtras-controls" % "17-r1",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.1.212",
    "ch.qos.logback" % "logback-classic" % "1.2.11",
    "org.scalatest" %% "scalatest" % "3.2.12" % Test
  )
}
