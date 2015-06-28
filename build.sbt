name := """MovieDatabase"""

version := "1.0"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava,
    PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions
)

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.google.code.gson" % "gson" % "2.3.1"


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
