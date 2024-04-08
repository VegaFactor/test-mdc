
import sbt._
import com.lightbend.cinnamon.sbt.Cinnamon

object Dependency {

  val scalaLangVersion = "2.13.11"

  val allResolvers = Seq(
    Resolver.mavenLocal,
    "Atlassian Releases"      at "https://maven.atlassian.com/public/",
    "Typesafe repository"     at "https://repo.typesafe.com/typesafe/releases/",
//    "lightbend-commercial"    at "https://repo.lightbend.com/commercial-releases",
    "lightbend-commercial"    at "https://repo.lightbend.com/pass/32y6h6qSbhhO4cZGe1luu_-a6GJEaUaRpPmWpU1HQ-7guTNn/commercial-releases",
    "Akka library repository" at "https://repo.akka.io/maven",
    Resolver.jcenterRepo,
    Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns),
//    Resolver.githubPackages("VegaFactor")
  )

  // app dependencies
  val playJsonVersion               = "2.10.4"
  val akkaVersion                   = "2.9.2"
  val logstashLogbackVersion        = "7.4"
  val logbackVersion                = "1.4.14"
  val cinnamonMetricsVersion        = "2.19.3"
  val jacksonScalaVersion           = "2.15.4"

  object ScalaLang {
    val reflect = "org.scala-lang" % "scala-reflect" % scalaLangVersion
  }

  object Akka {
    val core          = "com.typesafe.akka"             %% "akka-actor"                   % akkaVersion
    val coreTyped     = "com.typesafe.akka"             %% "akka-actor-typed"             % akkaVersion

    val slf4j         = "com.typesafe.akka"             %% "akka-slf4j"                   % akkaVersion
    val streams       = "com.typesafe.akka"             %% "akka-stream"                  % akkaVersion

    val jackson       = "com.typesafe.akka"             %% "akka-serialization-jackson"   % akkaVersion
    val jacksonScala  = "com.fasterxml.jackson.module"  %% "jackson-module-scala"         % jacksonScalaVersion
  }

  object PlayDeps {
    val json          = "com.typesafe.play" %% "play-json"                % playJsonVersion
  }

  object Metrics {
    val cinnamonJvm            = "com.lightbend.cinnamon" %  "cinnamon-chmetrics-jvm-metrics" % cinnamonMetricsVersion
  }

  object Logging {
    val logstashLogback = "net.logstash.logback" %  "logstash-logback-encoder" % logstashLogbackVersion exclude("com.fasterxml.jackson.core", "jackson-databind")
    val logbackClassic  = "ch.qos.logback"       %  "logback-classic"          % logbackVersion
  }

  val metricsDependencies = Seq(
    Cinnamon.library.cinnamonAkka,
    Cinnamon.library.cinnamonSlf4jMdc,
  )
}
