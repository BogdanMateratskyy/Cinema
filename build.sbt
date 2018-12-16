val dependancies = Seq (
  "com.microsoft.sqlserver" % "mssql-jdbc" % "7.0.0.jre8" % Test,
  "commons-cli" % "commons-cli" % "1.4",
  "org.apache.commons" % "commons-configuration2" % "2.1.1",
  "org.apache.commons" % "commons-text" % "1.2",
  "commons-beanutils" % "commons-beanutils" % "1.9.3"

)
lazy val root = (project in file(".")).settings(
  name := "Cinema",
  version := "0.1",
  scalaVersion := "2.12.8",
  libraryDependencies ++= dependancies 
)  