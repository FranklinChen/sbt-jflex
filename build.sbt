sbtPlugin              := true

name                   := "sbt-jflex"
organization           := "de.sciss"
version                := "0.4.0"
// scalaVersion           := "2.12.8"
sbtVersion in Global   := "1.2.7"
crossSbtVersions       := Seq("0.13.17", "1.2.7")
scalacOptions          := Seq("-deprecation", "-unchecked", "-feature" /* , "-Xsource:2.13" */)
description            := "An sbt plugin that generates code based on a JFlex specification"
homepage               := Some(url(s"https://git.iem.at/sciss/${name.value}"))
licenses               := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

libraryDependencies += "de.jflex" % "jflex" % "1.7.0" % Compile

// cf. https://github.com/sbt/sbt/issues/3305
scalaVersion := (CrossVersion partialVersion (sbtVersion in pluginCrossBuild).value match {
  case Some((0, 13)) => "2.10.6"
  case Some((1, _))  => "2.12.8"
  case _             => sys.error(s"Unhandled sbt version ${(sbtVersion in pluginCrossBuild).value}")
})

// ---- publishing ----

publishMavenStyle := true

publishTo :=
  Some(if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := { val n = name.value
<scm>
  <url>git@git.iem.at:sciss/{n}.git</url>
  <connection>scm:git:git@git.iem.at:sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>stefri</id>
    <name>Steffen Fritzsche</name>
    <url>http://scriptroom.de/</url>
  </developer>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
  <developer>
    <id>dlwh</id>
    <name>David Hall</name>
    <url>http://cs.berkeley.edu/~dlwh/</url>
  </developer>
</developers>
}
