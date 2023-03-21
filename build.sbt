name := "poolmate"
organization := "objektwerks"
version := "1.7-SNAPSHOT"
scalaVersion := "2.13.10"
libraryDependencies ++= {
  val slickVersion = "3.5.0-M2" // Still waiting to upgrade to Scala 3!
  Seq(
    "org.scalafx" %% "scalafx" % "19.0.0-R30",
    "org.jfxtras" % "jfxtras-controls" % "17-r1",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.1.214",
    "ch.qos.logback" % "logback-classic" % "1.4.6",
    "org.scalatest" %% "scalatest" % "3.2.15" % Test
  )
}
