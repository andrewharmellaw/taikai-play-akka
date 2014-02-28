import play.Project._

name := """taikai-play-akka"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "org.webjars" %% "webjars-play" % "2.2.0", 
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "com.typesafe.akka" % "akka-testkit_2.10" % "2.2.3" % "test"
)

playScalaSettings
