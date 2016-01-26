name := "scala-router"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-server" % "9.3.6.v20151106",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scalaj" %% "scalaj-http" % "2.1.0" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "com.lihaoyi" %% "scalatags" % "0.5.3"
)

