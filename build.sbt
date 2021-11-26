import sbt.Keys._
import Dependencies._
import Settings._

ThisBuild / scalaVersion := "2.12.15"
ThisBuild / organization := "org.locationtech.geotrellis"
ThisBuild / crossScalaVersions := List("2.12.15", "2.13.6")

lazy val root = Project("geotrellis-geomsa", file("."))
  .settings(commonSettings)
  .settings(
    name := "geotrellis-geomesa",
    initialize := {
      val curr = VersionNumber(sys.props("java.specification.version"))
      val req = SemanticSelector("=1.8")
      if (!curr.matchesSemVer(req)) {
        val log = Keys.sLog.value
        log.warn(s"Java $req required for GeoTools compatibility. Found Java $curr.\n" +
          "Please change the version of Java running sbt.")
      }
    },
    libraryDependencies ++= Seq(
      accumuloCore,
      geotrellisVector,
      geotrellisStore,
      geotrellisGeoTools,
      geomesaAccumuloJobs,
      geomesaAccumuloDatastore,
      geomesaUtils,
      apacheSpark("core").value % Provided,
      scalatest % Test
    ),
    Test / fork := true
  )