resolvers += sbt.Resolver.bintrayIvyRepo("typesafe", "sbt-plugins")

addDependencyTreePlugin
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.3")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.6.0")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.29")
