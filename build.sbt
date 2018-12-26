val dependancies = Seq (
  "mysql" % "mysql-connector-java" % "5.1.24",
  "commons-cli" % "commons-cli" % "1.4",
  "org.apache.commons" % "commons-configuration2" % "2.1.1",
  "org.apache.commons" % "commons-text" % "1.2",
  "commons-beanutils" % "commons-beanutils" % "1.9.3",
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.19",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19" % Test,
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.19" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.softwaremill.sttp" %% "core" % "1.5.1" % Test,
  "net.liftweb" %% "lift-json" % "3.2.0",
  "com.typesafe.play" %% "play-json" % "2.6.10"
)
lazy val root = (project in file(".")).settings(
  name := "Cinema",
  version := "0.1",
  scalaVersion := "2.12.8",
  libraryDependencies ++= dependancies 
)

enablePlugins(JavaAppPackaging)

dockerBaseImage := "anapsix/alpine-java"
dockerExposedPorts ++= Seq(9000)
