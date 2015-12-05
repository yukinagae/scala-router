import sbt._
import sbt.Keys._

object ScalarouterBuild extends Build {

  lazy val scalarouter = Project(
    id = "scala-router",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "scala-router",
      organization := "com.yukinagae",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2"
      // add other settings here
    )
  )
}
