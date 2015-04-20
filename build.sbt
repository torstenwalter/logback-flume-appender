import sbtrelease._
import ReleaseStateTransformations._

name := "logback-flume-appender"

organization := "com.teambytes.logback"

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.11.4", "2.10.4")

releaseSettings

ReleaseKeys.crossBuild := true

libraryDependencies ++= Seq(
  "org.apache.flume" % "flume-ng-sdk" % "1.5.0.1",
  "ch.qos.logback" % "logback-classic" % "1.1.1"
)

publishArtifact in Test := false

publishMavenStyle := true

pomIncludeRepository := { _ => false }

licenses := Seq("Apache License 2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

homepage := Some(url("https://github.com/grahamar/logback-flume-appender"))

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := <scm>
  <url>git@github.com:grahamar/logback-flume-appender.git</url>
  <connection>scm:git:git@github.com:grahamar/logback-flume-appender.git</connection>
</scm>
<developers>
  <developer>
    <id>grhodes</id>
    <name>Graham Rhodes</name>
    <url>https://github.com/grahamar</url>
  </developer>
</developers>

lazy val publishSignedAction = { st: State =>
  val extracted = Project.extract(st)
  val ref = extracted.get(thisProjectRef)
  extracted.runAggregated(com.typesafe.sbt.pgp.PgpKeys.publishSigned in Global in ref, st)
}

sbtrelease.ReleasePlugin.ReleaseKeys.releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts.copy(action = publishSignedAction),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

