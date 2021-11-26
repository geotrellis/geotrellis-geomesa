/*
 * Copyright (c) 2014 Azavea.
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

import sbt._
import sbt.Keys._

object Dependencies {
  object Version {
    val geotrellis = "3.6.0"
    val geomesa = "3.3.0"
    val accumulo    = "1.9.3"
  }

  private def ver(for212: String, for213: String) = Def.setting {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 12)) => for212
      case Some((2, 13)) => for213
      case _             => sys.error("not good")
    }
  }

  def apacheSpark(module: String) = Def.setting {
    "org.apache.spark" %% s"spark-$module" % ver("3.1.1", "3.2.0").value
  }

  def scalaReflect(version: String) = "org.scala-lang" % "scala-reflect" % version

  val scalatest = "org.scalatest" %% "scalatest" % "3.2.5"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val accumuloCore = "org.apache.accumulo" % "accumulo-core" % Version.accumulo
  val geotrellisVector = "org.locationtech.geotrellis" %% "geotrellis-vector" % Version.geotrellis
  val geotrellisGeoTools = "org.locationtech.geotrellis" %% "geotrellis-geotools" % Version.geotrellis
  val geotrellisStore = "org.locationtech.geotrellis" %% "geotrellis-store" % Version.geotrellis
  val geomesaJobs = "org.locationtech.geomesa" %% "geomesa-jobs" % Version.geomesa
  val geomesaAccumuloJobs = "org.locationtech.geomesa" %% "geomesa-accumulo-jobs" % Version.geomesa
  val geomesaAccumuloDatastore = "org.locationtech.geomesa" %% "geomesa-accumulo-datastore" % Version.geomesa
  val geomesaUtils = "org.locationtech.geomesa" %% "geomesa-utils" % Version.geomesa
  val squants = "org.typelevel" %% "squants" % "1.7.0"
  val newtype = "io.estatico" %% "newtype" % "0.4.4"
  val java8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1"

  // located in the OSGeo repo: https://repo.osgeo.org/repository/release/
  // 'works with' due to license issues
  val jaiCore = "javax.media" % "jai_core" % "1.1.3"
  val jaiCodec = "javax.media" % "jai_codec" % "1.1.3"
  val imageIo = "javax.media" % "jai_imageio" % "1.1"

  val imageioExtUtilities = "it.geosolutions.imageio-ext" % "imageio-ext-utilities" % "1.3.5"

  val worksWithDependencies = Seq(jaiCore, jaiCodec, imageIo, imageioExtUtilities).map(_ % Provided)
}
