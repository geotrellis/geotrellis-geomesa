/*
 * Copyright (c) 2018 Azavea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Dependencies._
import sbt._
import sbt.Keys._
import de.heikoseeberger.sbtheader.{CommentStyle, FileType}
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.{HeaderLicense, headerLicense, headerMappings}

import java.io.File

object Settings {
  object Repositories {
    val apacheSnapshots = "apache-snapshots" at "https://repository.apache.org/content/repositories/snapshots/"
    val eclipseReleases = "eclipse-releases" at "https://repo.eclipse.org/content/groups/releases"
    val osgeoReleases   = "osgeo-releases" at "https://repo.osgeo.org/repository/release/"
    val geosolutions    = "geosolutions" at "https://maven.geo-solutions.it/"
    val jitpack         = "jitpack" at "https://jitpack.io" // for https://github.com/everit-org/json-schema
    val ivy2Local       = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
    val mavenLocal      = Resolver.mavenLocal
    val maven           = DefaultMavenRepository
    val local           = Seq(ivy2Local, mavenLocal)
    val external        = Seq(osgeoReleases, maven, eclipseReleases, geosolutions, jitpack, apacheSnapshots)
    val all             = external ++ local
  }

  val commonScalacOptions = Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials",
    "-language:experimental.macros",
    "-feature",
    "-Ywarn-unused-import",
    "-target:jvm-1.8"
  )

  lazy val commonSettings = Seq(
    description := "geographic data processing library for high performance applications",
    licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("https://geotrellis.io")),
    scmInfo := Some(ScmInfo(url("https://github.com/locationtech/geotrellis"), "scm:git:git@github.com:locationtech/geotrellis.git")),
    scalacOptions ++= commonScalacOptions,
    publishMavenStyle := true,
    Test / publishArtifact := false,
    pomIncludeRepository := { _ => false },
    autoAPIMappings := true,
    Global / cancelable := true,
    Test / parallelExecution := false,

    publishTo := {
      val sonatype = "https://oss.sonatype.org/"
      val locationtech = "https://repo.eclipse.org/content/repositories"

      System.getProperty("release") match {
        case "locationtech" if isSnapshot.value =>
          Some("LocationTech Snapshot Repository" at s"${locationtech}/geotrellis-snapshots")
        case "locationtech" =>
          Some("LocationTech Release Repository" at s"${locationtech}/geotrellis-releases")
        case _ =>
          Some("Sonatype Release Repository" at s"${sonatype}service/local/staging/deploy/maven2")
      }
    },

    credentials ++= List(
      Path.userHome / ".ivy2" / ".credentials",
      Path.userHome / ".sbt" / ".credentials"
    ).filter(_.asFile.canRead).map(Credentials(_)),

    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("org.scalameta" % "semanticdb-scalac" % "4.4.28" cross CrossVersion.full),

    libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => Nil
      case Some((2, 12)) => Seq(
        compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
        "org.scala-lang.modules" %% "scala-collection-compat" % "2.4.2"
      )
        case x => sys.error(s"Encountered unsupported Scala version ${x.getOrElse("undefined")}")
    }),
    Compile / scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Seq("-Ymacro-annotations") // replaces paradise in 2.13
        case Some((2, 12)) => Seq("-Ypartial-unification") // required by Cats
        case x => sys.error(s"Encountered unsupported Scala version ${x.getOrElse("undefined")}")
    }),

    libraryDependencies += scalaReflect(scalaVersion.value),

    pomExtra := (
      <developers>
        <developer>
          <id>echeipesh</id>
          <name>Eugene Cheipesh</name>
          <url>https://github.com/echeipesh/</url>
        </developer>
        <developer>
          <id>pomadchin</id>
          <name>Grigory Pomadchin</name>
          <url>https://github.com/pomadchin/</url>
        </developer>
      </developers>
    ),

    externalResolvers := Settings.Repositories.all,
    headerLicense := Some(HeaderLicense.ALv2(java.time.Year.now.getValue.toString, "Azavea")),
    headerMappings := Map(
      FileType.scala -> CommentStyle.cStyleBlockComment.copy(
        commentCreator = { (text, existingText) => {
          // preserve year of old headers
          val newText = CommentStyle.cStyleBlockComment.commentCreator.apply(text, existingText)
          existingText.flatMap(_ => existingText.map(_.trim)).getOrElse(newText)
        } }
      )
    )
  )

  // excluded dependencies due to license issue
  // exclusion list is inspired by https://github.com/locationtech/geomesa/blob/geomesa_2.11-3.1.2/pom.xml#L1031
  lazy val excludedDependencies = List(
    ExclusionRule("javax.media", "jai_core"),
    ExclusionRule("javax.media", "jai_codec"),
    ExclusionRule("javax.media", "jai_imageio"),
    ExclusionRule("it.geosolutions.imageio-ext"),
    ExclusionRule("jgridshift", "jgridshift"),
    ExclusionRule("jgridshift", "jgridshift-core"),
    ExclusionRule("org.jaitools", "jt-zonalstats"),
    ExclusionRule("org.jaitools", "jt-utils")
  )

}