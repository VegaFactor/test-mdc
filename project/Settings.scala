import Dependency._

//import com.lightbend.cinnamon.sbt.Cinnamon


import sbt._
import sbt.Keys._
import sbtghpackages.GitHubPackagesPlugin.autoImport._

object Settings {
  val optimizerOpts: Seq[String] = {
    val enabled = sys.props.getOrElse("enableOptimizer", "false").toBoolean
    val options =
      if (enabled) {
        Seq(
          "-opt:inline:**",
          "-opt:local"
        )
      }
      else Seq.empty

    println(s"Compiling with scala optimizer options: [${options.mkString(", ")}]")

    options
  }

  val buildTime = new java.util.Date()
  val buildTimeStr = new java.text.SimpleDateFormat("MMMM dd yyyy HH:mm:ss").format(buildTime)
  val buildVersionIncrement = buildTime.getTime / 10000

  // disabled doc generating for the bundle commands
  lazy val commonSettings: Seq[SettingsDefinition] = Seq(
    ThisBuild / organization := "com.vegafactor",
    ThisBuild / version := "1.0-SNAPSHOT",
    ThisBuild / scalaVersion := scalaLangVersion,

    packageDoc / publishArtifact := false,
    Compile / doc / sources := Seq.empty,
    updateOptions := updateOptions.value.withCachedResolution(true),

    scalaModuleInfo := scalaModuleInfo.value map { _.withOverrideScalaVersion(true) },
    ThisBuild / Test / testOptions += Tests.Argument("-oF"),
    ThisBuild / Test / javaOptions ++= Seq("-Xmx800m"),
    Test / testForkedParallel := true,
    ThisBuild / scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-Ybackend-parallelism", "3",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Ymacro-annotations",
      "-language:higherKinds",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-Wconf:cat=other-implicit-type:s", // TODO enable before migrating to Scala 3
      //  "-Xfatal-warnings", TODO: enable
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:imports",
      "-Ypatmat-exhaust-depth", "50", // default is 40
    ) ++ optimizerOpts,
    maxErrors := 20,
    resolvers ++= Dependency.allResolvers,
    dependencyOverrides ++= Seq(
      ScalaLang.reflect,
      PlayDeps.json,
      Akka.slf4j,
      Akka.jackson,
      Akka.jacksonScala
    ),

    githubOwner       := "VegaFactor",
    githubRepository  := "capella",
    githubTokenSource := TokenSource.Environment("GITHUB_TOKEN") || TokenSource.GitConfig("github.token"),
  )

}
