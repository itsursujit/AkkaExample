import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtMultiJvm.multiJvmSettings
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

//////////////////////////////////////////////////////////////////////////////
// PROJECT INFO
//////////////////////////////////////////////////////////////////////////////


val ORGANIZATION    = "my.first.ddd.app"
val PROJECT_NAME    = "app"
val PROJECT_VERSION = "0.1-SNAPSHOT"
val SCALA_VERSION   = "2.12.4"
val AKKA_VERSION    = "2.5.8"
val AKKA_HTTP_VERSION    = "10.0.11"

//////////////////////////////////////////////////////////////////////////////
// DEPENDENCY VERSIONS
//////////////////////////////////////////////////////////////////////////////

val TYPESAFE_CONFIG_VERSION = "1.3.1"
val SCALATEST_VERSION       = "3.0.4"
val SLF4J_VERSION           = "1.7.25"
val LOGBACK_VERSION         = "1.2.3"
val SLICK_VERSION         = "3.2.1"

addCommandAlias("rebuild", ";clean; compile; package")


lazy val root = (project in file(".")).
  settings(
    version := PROJECT_VERSION,
    organization := ORGANIZATION,
    scalaVersion := SCALA_VERSION,

    scalacOptions in Compile ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint" , "-encoding", "utf8"),
    javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % AKKA_VERSION,
      "com.typesafe.akka" %% "akka-stream" % AKKA_VERSION,
      "com.typesafe.akka" %% "akka-http" % AKKA_HTTP_VERSION,
      //"com.typesafe.akka" %% "akka-http-spray-json" % AKKA_HTTP_VERSION,
      "com.typesafe.akka" %% "akka-remote" % AKKA_VERSION,
      "com.typesafe.akka" %% "akka-cluster" % AKKA_VERSION,
      "com.typesafe.akka" %% "akka-distributed-data" % AKKA_VERSION,
      "com.typesafe.akka" %% "akka-multi-node-testkit" % AKKA_VERSION,

      "de.heikoseeberger" %% "akka-http-json4s" % "1.20.0-RC1",
      "org.json4s" %% "json4s-ast" % "3.5.3",
      "org.json4s" %% "json4s-native" % "3.5.3",
      "org.json4s" %% "json4s-jackson" % "3.5.3",

      "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
      "com.softwaremill.macwire" %% "macros" % "2.3.0" % Provided,
      "org.mindrot" % "jbcrypt" % "0.4",
      "io.jsonwebtoken" % "jjwt" % "0.9.0",
      "io.scalaland" %% "chimney" % "0.1.6",
      "org.typelevel" %% "cats-core" % "1.0.1",

      "com.typesafe.slick" %% "slick" % SLICK_VERSION,
      "com.typesafe.slick" %% "slick-hikaricp" % SLICK_VERSION,
      "com.typesafe.slick" %% "slick-codegen" % SLICK_VERSION,
      "mysql" % "mysql-connector-java" % "6.0.6",
      "org.flywaydb" % "flyway-core" % "5.0.6",

      "com.typesafe"     % "config"          % TYPESAFE_CONFIG_VERSION,
      "ch.qos.logback"   % "logback-classic" % LOGBACK_VERSION,
      "ch.qos.logback"   % "logback-classic" % LOGBACK_VERSION % "runtime",


      "com.typesafe.akka" %% "akka-http-testkit" % AKKA_HTTP_VERSION,
      "org.mockito" % "mockito-core" % "1.10.19",
      "org.scalatest" %% "scalatest" % SCALATEST_VERSION
    ),
    fork in run := true,
    // disable parallel tests
    parallelExecution in Test := false,
    licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0"))),
    fork in Test := true
  )

//credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}
