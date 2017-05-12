name := "poolmate"
organization := "objektwerks"
version := "0.2-SNAPSHOT"
scalaVersion := "2.12.2"
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
libraryDependencies ++= {
  val slickVersion = "3.2.0"
  Seq(
    "org.scalafx" % "scalafx_2.12" % "8.0.102-R11",
    "org.jfxtras" % "jfxtras-controls" % "8.0-r5",
    "com.typesafe.slick" % "slick_2.12" % slickVersion,
    "com.typesafe.slick" % "slick-hikaricp_2.12" % slickVersion,
    "com.h2database" % "h2" % "1.4.192",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test"
  )
}
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))
scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-feature",
  "-Ywarn-unused-import",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings",
  "-Xlint:missing-interpolator",
  "-Xlint"
)
fork in test := true
javaOptions += "-server -Xss2m -Xmx2g"
clippyColorsEnabled := true
enablePlugins(JavaAppPackaging)