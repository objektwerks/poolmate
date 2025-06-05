name := "poolmate"
organization := "objektwerks"
version := "3.3-SNAPSHOT"
scalaVersion := "3.6.4" // Scala 3.7.1 still breaks ScalaFx!
libraryDependencies ++= {
  val slickVersion = "3.5.1"
  Seq(
    "org.scalafx" %% "scalafx" % "24.0.0-R35",
    "org.jfxtras" % "jfxtras-controls" % "17-r1",
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.h2database" % "h2" % "2.3.232",
    "ch.qos.logback" % "logback-classic" % "1.5.18",
    "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
}
scalacOptions ++= Seq(
  "-Wunused:all",
  "-unchecked", "-deprecation"
)
