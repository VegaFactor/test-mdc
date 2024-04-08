import Dependency._
import Settings._
import com.lightbend.cinnamon.sbt.Cinnamon

name := """test-MDC"""
organization := "com.vegafactor"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, Cinnamon)
  .settings(name := "test-mdc")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++=
      metricsDependencies ++
        Seq(
          guice,

          Akka.core,
          Akka.coreTyped,
          Akka.streams,
          Akka.slf4j,
          Akka.jacksonScala,
        )
  )
